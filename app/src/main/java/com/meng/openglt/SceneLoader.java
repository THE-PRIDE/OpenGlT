package com.meng.openglt;

import android.os.SystemClock;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class loads a 3D scena as an example of what can be done with the app
 * 
 * @author andresoviedo
 *
 */
public class SceneLoader {

	/**
	 * Parent component
	 */
	protected final ModelActivity parent;
	/**
	 * List of data objects containing info for building the opengl objects
	 */
	private List<Object3DData> objects = new ArrayList<>();
	/**
	 * Whether to draw using textures
	 */
	private boolean drawTextures = true;
	/**
	 * Light toggle feature: whether to draw using lights
	 */
	private boolean drawLighting = true;

	public SceneLoader(ModelActivity main) {
		this.parent = main;
	}

	public void init() {
		// Load object
		if (parent.getParamFile() != null || parent.getParamAssetDir() != null) {
			Handler.assets = parent.getAssets();
			Object3DBuilder.loadV6AsyncParallel(parent, parent.getParamFile(), parent.getParamAssetDir(),
					parent.getParamAssetFilename(), new Object3DBuilder.Callback() {
						long startTime = SystemClock.uptimeMillis();
						@Override
						public void onBuildComplete(List<Object3DData> datas) {
							for (Object3DData data : datas) {
								loadTexture(data, parent.getParamFile(), parent.getParamAssetDir());
							}
							final String elapsed = (SystemClock.uptimeMillis() - startTime)/1000+" secs";
							makeToastText("Load complete ("+elapsed+")", Toast.LENGTH_LONG);
						}

						@Override
						public void onLoadComplete(List<Object3DData> datas) {
							for (Object3DData data : datas) {
								addObject(data);
							}
						}

						@Override
						public void onLoadError(Exception ex) {
							Toast.makeText(parent.getApplicationContext(),
									"There was a problem building the model: " + ex.getMessage(), Toast.LENGTH_LONG)
									.show();
						}
					});
		}
	}

	private void makeToastText(final String text, final int toastDuration) {
		parent.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(parent.getApplicationContext(), text, toastDuration).show();
			}
		});
	}

	/**
	 * Hook for animating the objects before the rendering
	 */
	public void onDrawFrame(){
		if (objects.isEmpty()) return;
	}

	protected synchronized void addObject(Object3DData obj) {
		List<Object3DData> newList = new ArrayList<>(objects);
		newList.add(obj);
		this.objects = newList;
		requestRender();
	}

	private void requestRender() {
		parent.getgLView().requestRender();
	}

	public synchronized List<Object3DData> getObjects() {
		return objects;
	}

	public boolean isDrawTextures() {
		return drawTextures;
	}

	public boolean isDrawLighting() {
		return drawLighting;
	}

	public void loadTexture(Object3DData data, File file, String parentAssetsDir){
		if (data.getTextureData() == null && data.getTextureFile() != null){
			try {
				InputStream stream;
				if (file != null){
					File textureFile = new File(file.getParent(),data.getTextureFile());
					stream = new FileInputStream(textureFile);
				}
				else{
					stream = parent.getAssets().open(parentAssetsDir + "/" + data.getTextureFile());
				}
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				IOUtils.copy(stream,bos);
				stream.close();
			} catch (IOException ex) {
				makeToastText("Problem loading texture "+data.getTextureFile(), Toast.LENGTH_SHORT);
			}
		}
	}

}
