package com.mengyu.okDownload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.meng.openglt.R;

/**
 * Created by LMY on 18/8/24.
 * 下载功能测试界面
 */

public class DownloadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_main);

        DownloadManager.getInstance();
    }

}
