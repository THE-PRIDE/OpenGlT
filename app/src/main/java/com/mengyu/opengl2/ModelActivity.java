package com.mengyu.opengl2;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;

import com.meng.openglt.R;

import java.io.File;

/**
 */
public class ModelActivity extends Activity {

    private String paramAssetDir;
    private String paramAssetFilename;
    private String paramFilename;
    private ModelSurfaceView gLView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3d_model);

        gLView = findViewById(R.id.modelSfv);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            this.paramAssetDir = b.getString("assetDir");
            this.paramAssetFilename = b.getString("assetFilename");
            this.paramFilename = b.getString("currentDir");
        }
//        gLView = new ModelSurfaceView(this);
        gLView.getActivity(ModelActivity.this);
        gLView.getHolder().setFormat(PixelFormat.TRANSLUCENT);//为GLSurfaceView指定Alpha通道
//        gLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//控制是否自动刷新
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

    public ModelSurfaceView getgLView() {
        return gLView;
    }

}
