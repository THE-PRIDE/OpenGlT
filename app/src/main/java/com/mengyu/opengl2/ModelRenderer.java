package com.mengyu.opengl2;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ModelRenderer implements GLSurfaceView.Renderer {

    private ModelSurfaceView main;
    private int width;
    private int height;
    private Camera camera;
    private final float near = 1f;
    private final float far = 100f;

    private Object3DBuilder loaderUtils;
    private Object3DParser parse3D;
    private Map<byte[], Integer> textures = new HashMap<>();
    private final float[] modelProjectionMatrix = new float[16];
    private final float[] modelViewMatrix = new float[16];
    private final float[] mvpMatrix = new float[16];

    public ModelRenderer(ModelSurfaceView modelSurfaceView) {
        this.main = modelSurfaceView;
    }

    public float getNear() {
        return near;
    }

    public float getFar() {
        return far;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        //改背景色
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        camera = new Camera();
        loaderUtils = new Object3DBuilder();
        parse3D =  new Object3DParser(main.getModelActivity());
        parse3D.parse3D();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        this.width = width;
        this.height = height;
        GLES20.glViewport(0, 0, width, height);

        //设置观看模型的位置
        Matrix.setLookAtM(modelViewMatrix, 0,
                camera.xPos, camera.yPos, camera.zPos,
                camera.xView, camera.yView, camera.zView,
                camera.xUp, camera.yUp, camera.zUp);
        float ratio = (float) width / height;
        Matrix.frustumM(modelProjectionMatrix, 0, -ratio, ratio, -1, 1, getNear(), getFar());
        Matrix.multiplyMM(mvpMatrix, 0, modelProjectionMatrix, 0, modelViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        camera.animate();
        if (camera.hasChanged()) {
            Matrix.setLookAtM(modelViewMatrix, 0,
                    camera.xPos, camera.yPos, camera.zPos,
                    camera.xView, camera.yView, camera.zView,
                    camera.xUp, camera.yUp, camera.zUp);
            Matrix.multiplyMM(mvpMatrix, 0, modelProjectionMatrix, 0, modelViewMatrix, 0);
            camera.setChanged(false);
        }

        if (parse3D.getObjects() == null ){
            return;
        }
        Object3DData objData = parse3D.getObjects();
        try {
            Object3DEntity object3DEntity = loaderUtils.get3DEntity();
            Integer textureId = textures.get(objData.getTextureData());
            if (textureId == null && objData.getTextureData() != null) {
                ByteArrayInputStream textureIs = new ByteArrayInputStream(objData.getTextureData());
                textureId = GLUtil.loadTexture(textureIs);
                textureIs.close();
                textures.put(objData.getTextureData(), textureId);
            }
            object3DEntity.draw(objData, modelProjectionMatrix, modelViewMatrix,
                    textureId != null ? textureId : -1);
        } catch (Exception ex) {
            Log.e("ModelRenderer", "There was a problem rendering the object '" + "':" + ex.getMessage(), ex);
        }
        main.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Camera getCamera() {
        return camera;
    }
}