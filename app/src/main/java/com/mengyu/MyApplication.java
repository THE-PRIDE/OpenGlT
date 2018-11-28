package com.mengyu;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.mengyu.retrofitUtils.model.BroadMsg;

import java.util.List;

/**
 * Created by LMY on 18/8/24.
 * 用于全局配置
 */

public class MyApplication extends Application {

    //全局上下文，作用域整个应用程序生命周期
    private static MyApplication instance;
    //本地广播只能在应用程序内部传递，接收器也只能接收来自应用程序发出的广播；本地广播不能通过静态注册接收
    private LocalBroadcastManager localBroadcastManager;//retrofit 发送广播

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    public static MyApplication getInstance(){
        return instance;
    }

    public void sendBroadcast(BroadMsg broadMsg){
        Intent intent = new Intent(broadMsg.getAction());
//        intent.putExtra(NotifyMessage.CC_MSG_CONTENT,broadMsg);
        localBroadcastManager.sendBroadcast(intent);
    }



    @Override
    public void onTerminate() {
//        当终止应用程序对象时调用，不保证一定被调用，当程序是被内核终止以便为其他应用程序释放资源，那么将不会提醒，并且不调用应用程序的对象的onTerminate方法而直接终止进程。
        super.onTerminate();
    }

    public void getTopActivity(){

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);

        ComponentName name = taskInfos.get(0).topActivity;
        name.getPackageName();
    }
}
