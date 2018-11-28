package com.mengyu.opengl2;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;

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
//        this.paramFilename = Environment.getExternalStorageDirectory() + "/0000/android_1540429200_3030600738/jintiao.obj";

//        gLView = new ModelSurfaceView(this);
        gLView.getActivity(ModelActivity.this);
        gLView.getHolder().setFormat(PixelFormat.TRANSLUCENT);//为GLSurfaceView指定Alpha通道
        gLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//控制是否自动刷新
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
