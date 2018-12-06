package com.mengyu.fragmentCode;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meng.openglt.R;
import com.mengyu.Utils.LogUtils;

public class MoneyFragment extends Fragment{

    AppCompatActivity appCompatActivity;

    private static final String TAG = "MoneyFragment";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //表示activity和fragment已经完成绑定
        LogUtils.e(TAG,"onAttach");
        appCompatActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e(TAG,"onCreate");

        //初始化fragment，获取saveInstanceState保存的值
        //执行完此方法后，会先将上个fragment清除解绑后，再执行onCreateView
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 初始化fragment布局，initView;不执行耗时操作
        LogUtils.e(TAG,"onCreateView");

        return inflater.inflate(R.layout.fragment_money_layout,null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //onCreateView后调用
        LogUtils.e(TAG,"onViewCreated");

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //在该方法内可以进行与Activity交互的UI操作
        LogUtils.e(TAG,"onActivityCreated");

        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onStart() {
        //Fragment由不可见变为可见状态
        LogUtils.e(TAG,"onStart");

        super.onStart();
    }

    @Override
    public void onResume() {
        //Fragment处于活动状态，用户可与之交互
        LogUtils.e(TAG,"onResume");

        super.onResume();
    }

    @Override
    public void onPause() {
        //Fragment处于暂停状态，但依然可见，用户不能与之交互。
        LogUtils.e(TAG,"onPause");

        super.onPause();
    }

    @Override
    public void onStop() {
        //Fragment完全不可见
        LogUtils.e(TAG,"onStop");

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        //销毁与Fragment有关的视图，但未与Activity解除绑定，
        // 依然可以通过onCreateView方法重新创建视图。通常在ViewPager+Fragment的方式下会调用此方法。
        LogUtils.e(TAG,"onDestroyView");

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        //销毁Fragment
        LogUtils.e(TAG,"onDestroy");

        super.onDestroy();
    }

    @Override
    public void onDetach() {
        //解除与Activity的绑定。在onDestroy方法之后调用
        LogUtils.e(TAG,"onDetach");

        super.onDetach();
    }
}
