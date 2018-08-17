package com.meng.openglt;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mobileciticbank on 18/7/24.
 *
 */

public class MyRender implements GLSurfaceView.Renderer{

    private int program;
    private int vPosition;
    private int uColor;

    private int loadShader(int shaderType,String sourceCode){
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0){
            GLES20.glShaderSource(shader,sourceCode);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader,GLES20.GL_COMPILE_STATUS,compiled,0);
            if (compiled[0] == 0){
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    private int createProgram(String vertexSource,String fragmentSource){
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexSource);
        if (vertexShader == 0){
            return 0;
        }

        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentSource);
        if (pixelShader == 0){
            return 0;
        }
        int program = GLES20.glCreateProgram();
        if (program != 0){
            GLES20.glAttachShader(program,vertexShader);
            GLES20.glAttachShader(program,pixelShader);
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS,linkStatus,0);
            if (linkStatus[0] != GLES20.GL_TRUE){
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    private FloatBuffer getVertices(){
        float vertices[] = {
                0.0f,0.5f,
                -0.5f,-0.5f,
                0.5f,-0.5f
        };
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuf = vbb.asFloatBuffer();
        vertexBuf.put(vertices);
        vertexBuf.position(0);

        return vertexBuf;
    }



    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        program = createProgram(verticesShader,fragmentShader);
        vPosition = GLES20.glGetAttribLocation(program,"Vposition");
        uColor = GLES20.glGetAttribLocation(program,"uColor");
        GLES20.glClearColor(1.0f,0,0,1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        FloatBuffer vertices = getVertices();
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(program);
        GLES20.glVertexAttribPointer(vPosition,2,GLES20.GL_FLOAT,false,0,vertices);
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glUniform4f(uColor,0.0f,1.0f,0.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,3);
    }


    private static final String verticesShader
            = "attribute vec2 vPosition;              \n"
            + "void main(){                           \n"
            + "     gl_Position = vec4(vPosition,0,1);\n"
            + "}";

    private static final String fragmentShader
            = "precision mediump float;              \n"
            + "uniform vec4 uColor;                  \n"
            + "void main(){                          \n"
            + "     gl_FragColor = uColor;\n"
            + "}";

}















