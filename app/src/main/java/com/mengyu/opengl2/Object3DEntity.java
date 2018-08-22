package com.mengyu.opengl2;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

/**
 * Abstract class that implements all calls to opengl to draw objects
 * <p>
 * Subclasses must provide vertex shader and specify whether the shaders supports specific features
 *
 * @author andresoviedo
 */
public class Object3DEntity implements Object3D {

    // Transformations
    private final float[] mMatrix = new float[16];
    // mvp matrix
    private final float[] mvMatrix = new float[16];
    private final float[] mvpMatrix = new float[16];
    // OpenGL data
    protected final int mProgram;

    private double shift = -1d;

    public Object3DEntity() {
        String[] variables = {"vColor", "a_Position", "a_TexCoordinate", "a_Normal"};
        int vertexShader = GLUtil.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = GLUtil.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLUtil.createAndLinkProgram(vertexShader, fragmentShader, variables);
    }

    @Override
    public void draw(Object3DData obj, float[] pMatrix, float[] vMatrix, int textureId) {
        this.draw(obj, pMatrix, vMatrix, obj.getDrawMode(), textureId);
    }

    @Override
    public void draw(Object3DData obj, float[] pMatrix, float[] vMatrix, int drawMode, int textureId) {

        GLES20.glUseProgram(mProgram);

        float[] mMatrix = getMMatrix(obj);
        float[] mvMatrix = getMvMatrix(mMatrix, vMatrix);
        float[] mvpMatrix = getMvpMatrix(mvMatrix, pMatrix);

        setMvpMatrix(mvpMatrix);

        int mPositionHandle = setPosition(obj);

//        int mColorHandle = -1;
        setColor(obj);

        if (textureId != -1) {
            setTexture(obj, textureId);
        }

        int mNormalHandle;
        mNormalHandle = setNormals(obj);
        setMvMatrix(mvMatrix);

        setLightPos();//设置相机距离
        drawShape(obj, drawMode);

        GLES20.glDisableVertexAttribArray(mPositionHandle);

//        if (mColorHandle != -1) {
//            GLES20.glDisableVertexAttribArray(mColorHandle);
//        }

        if (mNormalHandle != -1) {
            GLES20.glDisableVertexAttribArray(mNormalHandle);
        }
    }

    public float[] getMMatrix(Object3DData obj) {

        // calculate object transformation
        Matrix.setIdentityM(mMatrix, 0);
        if (obj.getRotation() != null) {
            Matrix.rotateM(mMatrix, 0, obj.getRotation()[0], 1f, 0f, 0f);
            Matrix.rotateM(mMatrix, 0, obj.getRotation()[1], 0, 1f, 0f);
            Matrix.rotateM(mMatrix, 0, obj.getRotationZ(), 0, 0, 1f);
        }
        if (obj.getScale() != null) {
            Matrix.scaleM(mMatrix, 0, obj.getScaleX(), obj.getScaleY(), obj.getScaleZ());
        }
        if (obj.getPosition() != null) {
            Matrix.translateM(mMatrix, 0, obj.getPositionX(), obj.getPositionY(), obj.getPositionZ());
        }
        return mMatrix;
    }

    public float[] getMvMatrix(float[] mMatrix, float[] vMatrix) {
        Matrix.multiplyMM(mvMatrix, 0, vMatrix, 0, mMatrix, 0);
        return mvMatrix;
    }

    protected float[] getMvpMatrix(float[] mvMatrix, float[] pMatrix) {
        Matrix.multiplyMM(mvpMatrix, 0, pMatrix, 0, mvMatrix, 0);
        return mvpMatrix;
    }

    protected void setMvpMatrix(float[] mvpMatrix) {

        // get handle to shape's transformation matrix
        int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
        GLUtil.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLUtil.checkGlError("glUniformMatrix4fv");
    }

    protected void setColor(Object3DData obj) {

        // get handle to fragment shader's vColor member
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLUtil.checkGlError("glGetUniformLocation");

        // Set color for drawing the triangle
        float[] color = obj.getColor() != null ? obj.getColor() : DEFAULT_COLOR;
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        GLUtil.checkGlError("glUniform4fv");
    }

    protected int setPosition(Object3DData obj) {

        // get handle to vertex shader's a_Position member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        GLUtil.checkGlError("glGetAttribLocation");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLUtil.checkGlError("glEnableVertexAttribArray");

        FloatBuffer vertexBuffer = obj.getVertexArrayBuffer() != null ? obj.getVertexArrayBuffer()
                : obj.getVertexBuffer();
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE,
                vertexBuffer);
        GLUtil.checkGlError("glVertexAttribPointer");

        return mPositionHandle;
    }

    protected int setNormals(Object3DData obj) {
        int mNormalHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal");
        GLUtil.checkGlError("glGetAttribLocation");

        GLES20.glEnableVertexAttribArray(mNormalHandle);
        GLUtil.checkGlError("glEnableVertexAttribArray");

        // Pass in the normal information
        FloatBuffer buffer = obj.getVertexNormalsArrayBuffer() != null ? obj.getVertexNormalsArrayBuffer() : obj.getNormals();
        buffer.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, false, 0, buffer);

        return mNormalHandle;
    }

    protected void setLightPos() {
        int mLightPosHandle = GLES20.glGetUniformLocation(mProgram, "u_LightPos");
        // Pass in the light position in eye space.
        GLES20.glUniform3f(mLightPosHandle, 5.0f, 5.0f ,5.0f);
    }

    protected void setMvMatrix(float[] mvMatrix) {
        int mMVMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVMatrix");
        GLUtil.checkGlError("glGetUniformLocation");

        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mvMatrix, 0);
        GLUtil.checkGlError("glUniformMatrix4fv");
    }

    protected int setTexture(Object3DData obj, int textureId) {
        int mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture");
        GLUtil.checkGlError("glGetUniformLocation");

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLUtil.checkGlError("glActiveTexture");

        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLUtil.checkGlError("glBindTexture");

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        GLUtil.checkGlError("glUniform1i");

        int mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate");
        GLUtil.checkGlError("glGetAttribLocation");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        GLUtil.checkGlError("glEnableVertexAttribArray");

        // Prepare the triangle coordinate data
        obj.getTextureCoordsArrayBuffer().position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0,
                obj.getTextureCoordsArrayBuffer());
        GLUtil.checkGlError("glVertexAttribPointer");

        return mTextureCoordinateHandle;
    }

    protected void drawShape(Object3DData obj, int drawMode) {
        FloatBuffer vertexBuffer = obj.getVertexArrayBuffer() != null ? obj.getVertexArrayBuffer()
                : obj.getVertexBuffer();
        vertexBuffer.position(0);
        List<int[]> drawModeList = obj.getDrawModeList();
        IntBuffer drawOrderBuffer = obj.getDrawOrder();


        if (obj.isDrawUsingArrays()) {
            drawOrderBuffer = null;
        }

        if (drawModeList != null) {
            if (drawOrderBuffer == null) {
                for (int j = 0; j < drawModeList.size(); j++) {
                    int[] polygon = drawModeList.get(j);
                    if (drawMode == GLES20.GL_LINE_LOOP && polygon[2] > 3) {
                        for (int i = 0; i < polygon[2] - 2; i++) {
                            GLES20.glDrawArrays(drawMode, polygon[1] + i, 3);
                        }
                    } else {
                        GLES20.glDrawArrays(drawMode, polygon[1], polygon[2]);
                    }
                }
            } else {
                for (int i = 0; i < drawModeList.size(); i++) {
                    int[] drawPart = drawModeList.get(i);
                    int drawModePolygon = drawPart[0];
                    int vertexPos = drawPart[1];
                    int drawSizePolygon = drawPart[2];
                    drawOrderBuffer.position(vertexPos);
                    GLES20.glDrawElements(drawModePolygon, drawSizePolygon, GLES20.GL_UNSIGNED_INT, drawOrderBuffer);
                }
            }
        } else {
            if (drawOrderBuffer != null) {
//                if (drawSize <= 0) {
                    drawOrderBuffer.position(0);
                    GLES20.glDrawElements(drawMode, drawOrderBuffer.capacity(), GLES20.GL_UNSIGNED_INT,
                            drawOrderBuffer);
//                } else {
//                    for (int i = 0; i < drawOrderBuffer.capacity(); i += drawSize) {
//                        drawOrderBuffer.position(i);
//                        GLES20.glDrawElements(drawMode, drawSize, GLES20.GL_UNSIGNED_INT, drawOrderBuffer);
//                    }
//                }
            } else {
//                if (drawSize <= 0) {
                    int drawCount = vertexBuffer.capacity() / COORDS_PER_VERTEX;

                    if (this.shift >= 0) {
                        double rotation = ((SystemClock.uptimeMillis() % 10000) / 10000f) * (Math.PI * 2);

                        if (this.shift == 0d) {
                            this.shift = rotation;
                        }
                        drawCount = (int) ((Math.sin(rotation - this.shift + Math.PI / 2 * 3) + 1) / 2f * drawCount);
                    }
                    GLES20.glDrawArrays(drawMode, 0, drawCount);
//                } else {
//                    for (int i = 0; i < vertexBuffer.capacity() / COORDS_PER_VERTEX; i += drawSize) {
//                        GLES20.glDrawArrays(drawMode, i, drawSize);
//                    }
//                }
            }
        }
    }


    private final static String vertexShaderCode =
            "uniform mat4 u_MVPMatrix;\n" +
                    "attribute vec4 a_Position;\n" +
                    // color
                    "uniform vec4 vColor;\n" +
                    // texture variables
                    "attribute vec2 a_TexCoordinate;" +
                    "varying vec2 v_TexCoordinate;" +
                    // light variables
                    "uniform mat4 u_MVMatrix;\n" +
                    "uniform vec3 u_LightPos;\n" +
                    "attribute vec3 a_Normal;\n" +
                    // calculated color
                    "varying vec4 v_Color;\n" +
                    "void main() {\n" +
                    // texture
                    "  v_TexCoordinate = a_TexCoordinate;" +
                    // Transform the vertex into eye space.
                    "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);\n          " +
                    // Get a lighting direction vector from the light to the vertex.
                    "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);\n    " +
                    // Transform the normal's orientation into eye space.
                    "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));\n " +
                    // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
                    // pointing in the same direction then it will get max illumination.
                    "   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);\n   " +
                    // Attenuate the light based on distance.
                    "   float distance = length(u_LightPos - modelViewVertex);\n         " +
                    "   diffuse = diffuse * (1.0 / (1.0 + (0.05 * distance * distance)));\n" +
                    //  Add ambient lighting
                    "  diffuse = diffuse + 0.5;" +
                    // Multiply the color by the illumination level. It will be interpolated across the triangle.
                    "   v_Color = vColor * diffuse;\n" +
                    "   v_Color[3] = vColor[3];" + // correct alpha
                    "  gl_Position = u_MVPMatrix * a_Position;\n" +
                    "  gl_PointSize = 2.5;  \n" +
                    "}";
    // @formatter:on

    // @formatter:off
    private final static String fragmentShaderCode =
            "precision mediump float;\n" +
                    "varying vec4 v_Color;\n" +
                    // textures
                    "uniform sampler2D u_Texture;" +
                    "varying vec2 v_TexCoordinate;" +
                    //
                    "void main() {\n" +
                    "  gl_FragColor = v_Color * texture2D(u_Texture, v_TexCoordinate);" +
                    "}";
}
