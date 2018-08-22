package com.mengyu.opengl2;

import android.widget.Toast;

public class SceneLoader {

    protected ModelActivity parent;
    /**
     * List of data objects containing info for building the opengl objects
     */
    private Object3DData object;

    public SceneLoader(ModelActivity main) {
        this.parent = main;
    }

    public void init() {
        // Load object
        if (parent.getParamFile() != null || parent.getParamAssetDir() != null) {
            Object3DBuilder.loadV6AsyncParallel(parent, parent.getParamFile(), parent.getParamAssetDir(),
                    parent.getParamAssetFilename(), new Object3DBuilder.Callback() {
                        @Override
                        public void onBuildComplete(Object3DData data) {

                        }

                        @Override
                        public void onLoadComplete(Object3DData data) {
                            addObject(data);
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

    protected void addObject(Object3DData obj) {
        this.object = obj;
        requestRender();
    }

    private void requestRender() {
        parent.getgLView().requestRender();
    }

    public Object3DData getObjects() {
        return object;
    }

}
