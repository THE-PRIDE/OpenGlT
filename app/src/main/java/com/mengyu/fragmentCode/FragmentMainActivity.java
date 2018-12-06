package com.mengyu.fragmentCode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meng.openglt.R;

public class FragmentMainActivity extends AppCompatActivity implements View.OnClickListener{


    private MoneyFragment moneyFragment;
    private LiveFragment liveFragment;

    private TextView mTvMainMoney;
    private TextView mTvMainLive;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_main_layout);

        initView();
        initData();
        initListener();
    }

    private void initView(){
        mTvMainMoney = findViewById(R.id.tv_fragment_main_money);
        mTvMainLive = findViewById(R.id.tv_fragment_main_live);
    }

    private void initData(){

        moneyFragment = new MoneyFragment();
        liveFragment = new LiveFragment();

        fragmentManager = getSupportFragmentManager();

    }

    private void initListener(){
        mTvMainMoney.setOnClickListener(this);
        mTvMainLive.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_fragment_main_money:
                changeFragment(moneyFragment);
                break;
            case R.id.tv_fragment_main_live:
                changeFragment(liveFragment);
                break;
        }
    }


    private void changeFragment(Fragment fragment){


        FragmentTransaction transaction = fragmentManager.beginTransaction();

//        transaction.add(R.id.fyt_main_content,fragment);

//        transaction.show(fragment);

        transaction.replace(R.id.fyt_main_content,fragment);
        transaction.commitAllowingStateLoss();//saveInstanceState后调用commit会抛异常，此方法不会(允许状态丢失)
    }
}
