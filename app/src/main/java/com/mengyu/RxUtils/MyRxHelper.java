package com.mengyu.RxUtils;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by LMY on 18/8/17.
 * Rxjava 工具类
 */

public class MyRxHelper {

    public void RxTest() {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                e.onNext(1);
            }
        }).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onNext(Integer integer) {

                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }


    public void RxTest2(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        });
    }

    public void RxFlowable(){
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(FlowableEmitter<Object> e) throws Exception {

            }
        }, BackpressureStrategy.BUFFER).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });
    }

    public static void RxFlowableTest(){

         createFlowable(5).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e("TEST",integer+"");
            }
        });
    }

    public static void RxFlowableTest2(){

        createFlowable(10).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * 创建被观察者
     * @param t 参数
     * @param <T> 参数对象
     * @return 返回被观察者实例
     */
    public static <T> Flowable<T> createFlowable(final T t){
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> e) throws Exception {
                e.onNext(t);
            }
        },BackpressureStrategy.BUFFER);
    }
}
