package com;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.meng.openglt.R;
import com.mengyu.zipUtils.ZipUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LMY on 18/8/16.
 *  测试解压Activity
 */

public class ZipTestActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTvZipTest;

    private String zipPath;
    private String unZipPath;
    private List<File> unZipList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_test);
        initView();
        initData();
        initListener();
    }

    void initView(){
        mTvZipTest = findViewById(R.id.tv_zip_test);
    }

    void initData(){
        zipPath = Environment.getExternalStorageDirectory()+"/0000/纪念章.zip";
        unZipPath = Environment.getExternalStorageDirectory()+"/0000/纪念章";
      }

    void initListener(){
        mTvZipTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_zip_test:
                Log.e("ZIP",zipPath);
                try {
                    ZipUtils.UnZipFolder(zipPath,unZipPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


}
