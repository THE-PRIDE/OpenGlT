package com.mengyu.opengl2;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;


/**
 *
 */
public class ModelSurfaceView extends GLSurfaceView {

	private ModelActivity parent;
	private ModelRenderer mRenderer;
	private TouchController touchHandler;

	public ModelSurfaceView(ModelActivity parent) {
		super(parent);
		this.parent = parent;
		setEGLContextClientVersion(2);
		mRenderer = new ModelRenderer(this);
		setRenderer(mRenderer);
		touchHandler = new TouchController(this, mRenderer);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return touchHandler.onTouchEvent(event);
	}

	public ModelActivity getModelActivity() {
		return parent;
	}


}