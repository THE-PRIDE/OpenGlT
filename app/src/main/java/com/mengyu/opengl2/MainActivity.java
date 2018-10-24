package com.mengyu.opengl2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.meng.openglt.R;

import java.io.File;


public class MainActivity extends AppCompatActivity {


//    String absolutePath = Environment.getExternalStorageDirectory() + "/0000/纪念章";
    String absolutePath = Environment.getExternalStorageDirectory() + "/0000/jintiao";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, ModelActivity.class);
        Bundle b = new Bundle();
//        b.putString("assetDir", "models");
//        b.putString("assetFilename", "xianglian.obj");
//        b.putString("assetFilename", "jinianzhang_005.obj");
//        b.putString("assetFilename", "jinianzhang_006.obj");
//        String unZipPath = Environment.getExternalStorageDirectory() + "/0000/纪念章/jinianzhang_006.obj";
//        Log.e("TEST", unZipPath);
//        String fileName = getFileName(absolutePath);/**/
        b.putString("currentDir", getFileName(absolutePath));

//        Log.e("TEST", unZipPath);
//        this.paramAssetDir = b.getString("assetDir");
//        this.paramAssetFilename = b.getString("assetFilename");
//        this.paramFilename = b.getString("uri");
        intent.putExtras(b);
        startActivity(intent);

    }

    public static String getFileName(String fileAbsolutePath) {
        File file = new File(fileAbsolutePath);
        String fileName = "";

        if (!file.exists() || !file.isDirectory()) {
            return "";
        }
        File[] subFile = file.listFiles();

        if (subFile == null){
            return "";
        }

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                fileName = subFile[iFileLength].getName();
//                fileName = fileName.endsWith(".obj") ?  "a" : "a";
                if (fileName.endsWith(".obj"))
                    break;
            }
        }
        return fileAbsolutePath + File.separator +fileName;
    }
}
