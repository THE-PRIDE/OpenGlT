package com.mengyu.opengl2;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.mengyu.opengl2.WavefrontLoader.FaceMaterials;
import com.mengyu.opengl2.WavefrontLoader.Faces;
import com.mengyu.opengl2.WavefrontLoader.Materials;
import com.mengyu.opengl2.WavefrontLoader.Tuple3;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the basic 3D data necessary to build the 3D object
 *
 * @author andres
 */
public class Object3DData {

    // opengl version to use to draw this object
    private int version = 5;
    /**
     * The directory where the files reside so we can build referenced files in the model like material and textures
     * files
     */
    private File currentDir;
    /**
     * The assets directory where the files reside so we can build referenced files in the model like material and
     * textures files
     */
    private String assetsDir;
    private String id;
    private boolean drawUsingArrays = false;
    private boolean flipTextCoords = true;

    private float[] color;
    /**
     * The minimum thing we can draw in space is a vertex (or point).
     * This drawing mode uses the vertexBuffer
     */
    private int drawMode = GLES20.GL_POINTS;

    // Model data
    private FloatBuffer vertexBuffer = null;
    private FloatBuffer vertexNormalsBuffer = null;
    private IntBuffer drawOrderBuffer = null;
    private ArrayList<Tuple3> texCoords;
    private Faces faces;
    private FaceMaterials faceMats;
    private Materials materials;
    private String textureFile;

    // Processed arrays
    private FloatBuffer vertexArrayBuffer = null;
    private FloatBuffer vertexColorsArrayBuffer = null;
    private FloatBuffer vertexNormalsArrayBuffer = null;
    private FloatBuffer textureCoordsArrayBuffer = null;
    private List<int[]> drawModeList = null;
    private byte[] textureData = null;


    // Transformation data
    protected float[] position = new float[]{0f, 0f, 0f};
    protected float[] rotation = new float[]{0f, 0f, 0f};
    protected float[] scale = new float[]{1, 1, 1};
    protected float[] modelMatrix = new float[16];

    {
        Matrix.setIdentityM(modelMatrix, 0);
    }

    // Async Loader
    private WavefrontLoader.ModelDimensions modelDimensions;
    private WavefrontLoader loader;

    public Object3DData(FloatBuffer vertexArrayBuffer) {
        this.vertexArrayBuffer = vertexArrayBuffer;
        this.version = 1;
    }

    public Object3DData(FloatBuffer verts, FloatBuffer normals, ArrayList<Tuple3> texCoords, Faces faces,
                        FaceMaterials faceMats, Materials materials) {
        super();
        this.vertexBuffer = verts;
        this.vertexNormalsBuffer = normals;
        this.texCoords = texCoords;
        this.faces = faces;  // parameter "faces" could be null in case of async loading
        this.faceMats = faceMats;
        this.materials = materials;
    }

    public void setLoader(WavefrontLoader loader) {
        this.loader = loader;
    }


    public WavefrontLoader getLoader() {
        return loader;
    }

    public void setDimensions(WavefrontLoader.ModelDimensions modelDimensions) {
        this.modelDimensions = modelDimensions;
    }

    public int getVersion() {
        return version;
    }

    public Object3DData setVersion(int version) {
        this.version = version;
        return this;
    }

    public Object3DData setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public float[] getColor() {
        return color;
    }

    public Object3DData setColor(float[] color) {
        this.color = color;
        return this;
    }

    public int getDrawMode() {
        return drawMode;
    }

    public Object3DData setDrawMode(int drawMode) {
        this.drawMode = drawMode;
        return this;
    }

    public byte[] getTextureData() {
        return textureData;
    }

    public void setTextureData(byte[] textureData) {
        this.textureData = textureData;
    }

    public float[] getPosition() {
        return position;
    }

    public float getPositionX() {
        return position != null ? position[0] : 0;
    }

    public float getPositionY() {
        return position != null ? position[1] : 0;
    }

    public float getPositionZ() {
        return position != null ? position[2] : 0;
    }

    public float[] getRotation() {
        return rotation;
    }

    public float getRotationX() {
        return rotation[0];
    }

    public float getRotationY() {
        return rotation[1];
    }

    public float getRotationZ() {
        return rotation[2];
    }

    public Object3DData setScale(float[] scale) {
        this.scale = scale;
        updateModelMatrix();
        return this;
    }

    public float[] getScale() {
        return scale;
    }

    public float getScaleX() {
        return getScale()[0];
    }

    public float getScaleY() {
        return getScale()[1];
    }

    public float getScaleZ() {
        return getScale()[2];
    }

    private void updateModelMatrix() {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.setRotateM(modelMatrix, 0, getRotationX(), 1, 0, 0);
        Matrix.setRotateM(modelMatrix, 0, getRotationY(), 0, 1, 0);
        Matrix.setRotateM(modelMatrix, 0, getRotationY(), 0, 0, 1);
        Matrix.scaleM(modelMatrix, 0, getScaleX(), getScaleY(), getScaleZ());
        Matrix.translateM(modelMatrix, 0, getPositionX(), getPositionY(), getPositionZ());
    }

    public IntBuffer getDrawOrder() {
        return drawOrderBuffer;
    }

    public File getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(File currentDir) {
        this.currentDir = currentDir;
    }

    public void setAssetsDir(String assetsDir) {
        this.assetsDir = assetsDir;
    }

    public String getAssetsDir() {
        return assetsDir;
    }

    public boolean isDrawUsingArrays() {
        return drawUsingArrays;
    }

    public boolean isFlipTextCoords() {
        return flipTextCoords;
    }

    public Object3DData setDrawUsingArrays(boolean drawUsingArrays) {
        this.drawUsingArrays = drawUsingArrays;
        return this;
    }

    public FloatBuffer getVerts() {
        return vertexBuffer;
    }

    public FloatBuffer getNormals() {
        return vertexNormalsBuffer;
    }

    public ArrayList<Tuple3> getTexCoords() {
        return texCoords;
    }

    public Faces getFaces() {
        return faces;
    }

    public FaceMaterials getFaceMats() {
        return faceMats;
    }

    public Materials getMaterials() {
        return materials;
    }

    // -------------------- Buffers ---------------------- //

    public FloatBuffer getVertexBuffer() {
        return vertexBuffer;
    }

    public FloatBuffer getVertexArrayBuffer() {
        return vertexArrayBuffer;
    }

    public Object3DData setVertexArrayBuffer(FloatBuffer vertexArrayBuffer) {
        this.vertexArrayBuffer = vertexArrayBuffer;
        return this;
    }

    public FloatBuffer getVertexNormalsArrayBuffer() {
        return vertexNormalsArrayBuffer;
    }

    public Object3DData setVertexNormalsArrayBuffer(FloatBuffer vertexNormalsArrayBuffer) {
        this.vertexNormalsArrayBuffer = vertexNormalsArrayBuffer;
        return this;
    }

    public FloatBuffer getTextureCoordsArrayBuffer() {
        return textureCoordsArrayBuffer;
    }

    public Object3DData setTextureCoordsArrayBuffer(FloatBuffer textureCoordsArrayBuffer) {
        this.textureCoordsArrayBuffer = textureCoordsArrayBuffer;
        return this;
    }

    public List<int[]> getDrawModeList() {
        return drawModeList;
    }

    public Object3DData setVertexColorsArrayBuffer(FloatBuffer vertexColorsArrayBuffer) {
        this.vertexColorsArrayBuffer = vertexColorsArrayBuffer;
        return this;
    }

    /**
     * Position the model so it's center is at the origin, and scale it so its longest dimension is no bigger than
     * maxSize.
     */
    public void centerScale() {
        // calculate a scale factor
        float scaleFactor = 1.0f;
        float largest = modelDimensions.getLargest();
        if (largest != 0.0f)
            scaleFactor = (1.0f / largest);
        // get the model's center point
        Tuple3 center = modelDimensions.getCenter();
        // modify the model's vertices
        float x0, y0, z0;
        float x, y, z;
        FloatBuffer vertexBuffer = getVertexBuffer() != null ? getVertexBuffer() : getVertexArrayBuffer();
        for (int i = 0; i < vertexBuffer.capacity() / 3; i++) {
            x0 = vertexBuffer.get(i * 3);
            y0 = vertexBuffer.get(i * 3 + 1);
            z0 = vertexBuffer.get(i * 3 + 2);
            x = (x0 - center.getX()) * scaleFactor;
            vertexBuffer.put(i * 3, x);
            y = (y0 - center.getY()) * scaleFactor;
            vertexBuffer.put(i * 3 + 1, y);
            z = (z0 - center.getZ()) * scaleFactor;
            vertexBuffer.put(i * 3 + 2, z);
        }
    }

}
