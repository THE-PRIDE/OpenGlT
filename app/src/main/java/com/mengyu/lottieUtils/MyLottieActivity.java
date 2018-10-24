package com.mengyu.lottieUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.meng.openglt.R;

import java.util.Random;

/**
 * Created by LMY on 18/8/21.
 * Lottie测试
 *
 * resource download：https://www.lottiefiles.com/popular
 */

public class MyLottieActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_main);

        int a = new Random().nextInt(3) + 1;

        for (int i = 0;i<100;i++){
            Log.e("Test",a+"");
        }
    }


}
