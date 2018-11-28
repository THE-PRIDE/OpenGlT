package com.mengyu.timeCount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.meng.openglt.R;
import com.mengyu.MyApplication;

import java.util.Timer;
import java.util.TimerTask;

public class TimerCountActivity extends AppCompatActivity {



    private Button mBtbTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_hw_video_main);

        setContentView(R.layout.activity_timercount_f);

        mBtbTest = findViewById(R.id.btn_test1);
        mBtbTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerCountActivity.this,TimerCountActivity2.class);
                startActivity(intent);
            }
        });
    }



}
