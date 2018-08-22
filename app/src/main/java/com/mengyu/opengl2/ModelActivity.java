package com.mengyu.opengl2;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;

import java.io.File;

/**
 */
public class ModelActivity extends Activity {

    private String paramAssetDir;
    private String paramAssetFilename;
    private String paramFilename;
    private ModelSurfaceView gLView;
    private SceneLoader scene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            this.paramAssetDir = b.getString("assetDir");
            this.paramAssetFilename = b.getString("assetFilename");
            this.paramFilename = b.getString("uri");
        }
        gLView = new ModelSurfaceView(this);
        setContentView(gLView);
        gLView.getHolder().setFormat(PixelFormat.TRANSLUCENT);//为GLSurfaceView指定Alpha通道

        scene = new SceneLoader(this);
        scene.init();
    }

    public File getParamFile() {
        return getParamFilename() != null ? new File(getParamFilename()) : null;
    }

    public String getParamAssetDir() {
        return paramAssetDir;
    }

    public String getParamAssetFilename() {
        return paramAssetFilename;
    }

    public String getParamFilename() {
        return paramFilename;
    }

    public SceneLoader getScene() {
        return scene;
    }

    public ModelSurfaceView getgLView() {
        return gLView;
    }

}
