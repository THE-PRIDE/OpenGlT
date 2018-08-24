package com.meng.openglt;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ModelRenderer implements GLSurfaceView.Renderer {

	private final static String TAG = ModelRenderer.class.getName();

	// 3D window (parent component)
	private ModelSurfaceView main;
	// width of the screen
	private int width;
	// height of the screen
	private int height;
	// Out point of view handler
	private Camera camera;
	// frustrum - nearest pixel
	private final float near = 1f;
	// frustrum - fartest pixel
	private final float far = 100f;

	private Object3DBuilder drawer;
	// The wireframe associated shape (it should be made of lines only)
	// The loaded textures
	private Map<byte[], Integer> textures = new HashMap<>();
	// 3D matrices to project our 3D world
	private final float[] modelProjectionMatrix = new float[16];
	private final float[] modelViewMatrix = new float[16];
	// mvpMatrix is an abbreviation for "Model View Projection Matrix"
	private final float[] mvpMatrix = new float[16];

	// light position required to render with lighting
	private float[] lightPosInEyeSpace = new float[4];

	/**
	 * Construct a new renderer for the specified surface view
	 *
	 * @param modelSurfaceView
	 *            the 3D window
	 */
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
		// Set the background frame color
		float[] backgroundColor = main.getModelActivity().getBackgroundColor();
//		GLES20.glClearColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
		GLES20.glClearColor(1,1,1,0);

		// Use culling to remove back faces.
		// Don't remove back faces so we can see them
		// GLES20.glEnable(GLES20.GL_CULL_FACE);

		// Enable depth testing for hidden-surface elimination.
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		// Enable blending for combining colors when there is transparency
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		// Lets create our 3D world components
		camera = new Camera();

		// This component will draw the actual models using OpenGL
		drawer = new Object3DBuilder();
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		this.width = width;
		this.height = height;
		// Adjust the viewport based on geometry changes, such as screen rotation
		GLES20.glViewport(0, 0, width, height);

		// INFO: Set the camera position (View matrix)
		// The camera has 3 vectors (the position, the vector where we are looking at, and the up position (sky)
		Matrix.setLookAtM(modelViewMatrix, 0,
				camera.xPos, camera.yPos, camera.zPos,
				camera.xView, camera.yView, camera.zView,
				camera.xUp, camera.yUp, camera.zUp);

		// the projection matrix is the 3D virtual space (cube) that we want to project
		float ratio = (float) width / height;
		Log.d(TAG, "projection: [" + -ratio + "," + ratio + ",-1,1]-near/far[1,10]");
		Matrix.frustumM(modelProjectionMatrix, 0, -ratio, ratio, -1, 1, getNear(), getFar());
		// Calculate the projection and view transformation
		Matrix.multiplyMM(mvpMatrix, 0, modelProjectionMatrix, 0, modelViewMatrix, 0);
	}

	@Override
	public void onDrawFrame(GL10 unused) {

		// Draw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		// recalculate mvp matrix according to where we are looking at now
		camera.animate();
		if (camera.hasChanged()) {
			Matrix.setLookAtM(modelViewMatrix, 0,
					camera.xPos, camera.yPos, camera.zPos,
					camera.xView, camera.yView, camera.zView,
					camera.xUp, camera.yUp, camera.zUp);
			// Log.d("Camera", "Changed! :"+camera.ToStringVector());
			Matrix.multiplyMM(mvpMatrix, 0, modelProjectionMatrix, 0, modelViewMatrix, 0);
			camera.setChanged(false);
		}
		SceneLoader scene = main.getModelActivity().getScene();
		if (scene == null) {
			// scene not ready
			return;
		}
		// camera should know about objects that collision with it
		camera.setScene(scene);
		// animate scene
		scene.onDrawFrame();

        lightPosInEyeSpace[0] = 5.0f;
        lightPosInEyeSpace[1] = 5.0f;
        lightPosInEyeSpace[2] = 5.0f;
        lightPosInEyeSpace[3] = 5.0f;
		List<Object3DData> objects = scene.getObjects();
		for (int i=0; i<objects.size(); i++) {
			Object3DData objData = null;
			try {
				objData = objects.get(i);
				Object3D drawerObject = drawer.getDrawer(objData, scene.isDrawTextures(), scene.isDrawLighting());
				Integer textureId = textures.get(objData.getTextureData());
				if (textureId == null && objData.getTextureData() != null) {
					ByteArrayInputStream textureIs = new ByteArrayInputStream(objData.getTextureData());
					textureId = GLUtil.loadTexture(textureIs);
					textureIs.close();
					textures.put(objData.getTextureData(), textureId);
				}
				drawerObject.draw(objData, modelProjectionMatrix, modelViewMatrix,
						textureId != null ? textureId : -1, lightPosInEyeSpace);
			} catch (Exception ex) {
				Log.e("ModelRenderer","There was a problem rendering the object '"+objData.getId()+"':"+ex.getMessage(),ex);
			}
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