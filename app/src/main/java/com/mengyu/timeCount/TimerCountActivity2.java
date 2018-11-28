package com.mengyu.timeCount;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.meng.openglt.R;
import com.mengyu.MyApplication;

import java.util.Timer;
import java.util.TimerTask;

public class TimerCountActivity2 extends AppCompatActivity {


    private int num;
    private Timer timer;
    private Timer timer2;

    private Button mBtnStart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timercount_s);

        beginTimer();

        mBtnStart = findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginTimer2();
            }
        });

        MyApplication.getInstance().getTopActivity();

    }

    @Override
    protected void onStop() {
        cleanTimer();
        super.onStop();
    }

    private void initView() {

    }

    private void initData() {

    }

    private void initListener() {

    }

    /**
     * 开启任务管理器
     */
    public void beginTimer2() {
        if (null == timer2) {
            timer2 = new Timer();
            timer2.schedule(task2, 1000, 1000);
        }
    }


    /**
     * 开启任务管理器
     */
    public void beginTimer() {
        if (null == timer) {
            timer = new Timer();
//            try{
                timer.schedule(task, 1000, 1000);

//            }catch (Exception e){
//                e.printStackTrace();
//            }
        }
    }

    public void cleanTimer() {
        //计时器开启，未走完，下次进入需置空，防止下次计时器不启动
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    /**
     * 断网任务管理器
     */
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (num == 150) {

            } else {
                num++;
                Log.e("TIME1",num+"");
            }
        }
    };

    /**
     * 断网任务管理器
     */
    private TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            if (num == 150) {

            } else {
                num++;
                Log.e("TIME2",num+"");
            }
        }
    };

}