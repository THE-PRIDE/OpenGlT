package com.mengyu.RxUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.meng.openglt.R;
import com.mengyu.pdfrender.PDFActivity;

/**
 * Created by LMY on 18/8/17.
 * rxjava 测试界面
 */

public class RxJavaActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvRxjavaTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_main);
        initView();
        initData();
        initListener();
    }

    void initView(){
        mTvRxjavaTest = findViewById(R.id.tv_rxjava_test);
    }

    void initData(){

    }

    void initListener(){
        mTvRxjavaTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_rxjava_test:
                Intent intent = new Intent(RxJavaActivity.this, PDFActivity.class);
                startActivity(intent);
//                MyRxHelper.RxFlowableTest();
                break;
        }
    }
}
