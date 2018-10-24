package com.mengyu.okDownload;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.meng.openglt.R;
import com.mengyu.fileUtils.FileUtils;

import java.io.File;
import java.sql.Time;
import java.util.Calendar;

/**
 * Created by LMY on 18/8/24.
 * 下载功能测试界面
 */

public class DownloadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_main);

//        DownloadManager.getInstance();

//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.STORAGE},0);

        Calendar ac = Calendar.getInstance();
         int b = ac.get(Calendar.HOUR_OF_DAY);
        int c = ac.get(Calendar.MINUTE);
        int d = ac.get(Calendar.SECOND);
        int e = ac.get(Calendar.MILLISECOND);

        Log.e("Test",  ":" + b + ":" + c + ":" + d + ":" + e);

//        try {
//            FileUtils.writeTxtToFile("按时交付螺丝哦啊花覅偶少发货哦未啊时候覅偶问候if叫我if接欧文");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
