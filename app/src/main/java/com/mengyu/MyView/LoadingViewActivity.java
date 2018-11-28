package com.mengyu.MyView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.meng.openglt.R;

public class LoadingViewActivity extends AppCompatActivity {

    private LoadingView mLoadingView;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refresh(msg.what);
            if (msg.what == 360)
                Toast.makeText(LoadingViewActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_main);
        mLoadingView = findViewById(R.id.m_loadingview);


//        for (int a = 10; a <= 360; a = a+10) {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Log.e("TEST",a+"");
//
//            mLoadingView.dataNotify(a);
//            mLoadingView.invalidate();
//        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int a = 10; a <= 600; a = a + 10) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int i = a*360/600;
                    Log.e("TEST", i + "--" +a);
                    handler.sendEmptyMessage(i);
                }
            }
        }).start();
    }

    private void refresh(int a) {
        mLoadingView.dataNotify(a);
        mLoadingView.invalidate();
    }


}
