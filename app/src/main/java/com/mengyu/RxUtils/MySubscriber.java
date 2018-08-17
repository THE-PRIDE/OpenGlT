package com.mengyu.RxUtils;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by LMY on 18/8/17.
 * 响应类
 */

public class MySubscriber extends ResourceSubscriber {

    @Override
    protected void onStart() {
        super.onStart();
        //调用request方法后，可能马上会触发onNext方法，所以应确定在此方法执行
        //完所有初始化工作之后，再调用request方法；
        initData();
        request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Object o) {

        dispose();
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }

    void initData(){

    }
}
