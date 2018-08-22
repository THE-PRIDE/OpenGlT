package com.mengyu.opengl2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.meng.openglt.R;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AssetManager assets = getApplicationContext().getAssets();
//        try {
//            InputStream inputStream = assets.open("");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Intent intent = new Intent(MainActivity.this,ModelActivity.class);
        Bundle b = new Bundle();
        b.putString("assetDir", "models");
//        b.putString("assetFilename", "xianglian.obj");
//        b.putString("assetFilename", "jinianzhang_005.obj");
        b.putString("assetFilename", "jinianzhang_006.obj");
        b.putString("immersiveMode", "true");
        intent.putExtras(b);
        startActivity(intent);

    }
}
