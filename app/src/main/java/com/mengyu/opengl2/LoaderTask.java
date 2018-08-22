package com.mengyu.opengl2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * This component allows loading the model without blocking the UI.
 *
 * @author andresoviedo
 */
public abstract class LoaderTask extends AsyncTask<Void, Integer, Object3DData> {

	/**
	 * Callback to notify of events
	 */
	protected final Object3DBuilder.Callback callback;
	/**
	 * The dialog that will show the progress of the loading
	 */
	protected final ProgressDialog dialog;
	protected Exception error;



	public LoaderTask(Activity parent, Object3DBuilder.Callback callback) {
		this.dialog = new ProgressDialog(parent);
		this.callback = callback;
	}


	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.dialog.setMessage("Loading...");
		this.dialog.setCancelable(false);
		this.dialog.show();
	}

	@Override
	protected Object3DData doInBackground(Void... params) {
		try {
			Object3DData data = build();
			callback.onLoadComplete(data);
			build(data);
			return  data;
		} catch (Exception ex) {
			error = ex;
			return null;
		}
	}

	protected abstract Object3DData build() throws Exception;

	protected abstract void build(Object3DData data) throws Exception;

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		switch (values[0]) {
			case 0:
				this.dialog.setMessage("Analyzing model...");
				break;
			case 1:
				this.dialog.setMessage("Allocating memory...");
				break;
			case 2:
				this.dialog.setMessage("Loading data...");
				break;
			case 3:
				this.dialog.setMessage("Scaling object...");
				break;
			case 4:
				this.dialog.setMessage("Building 3D model...");
				break;
			case 5:
				break;
		}
	}

	@Override
	protected void onPostExecute(Object3DData data) {
		super.onPostExecute(data);
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		if (error != null) {
			callback.onLoadError(error);
		} else {
			callback.onBuildComplete(data);
		}
	}

}