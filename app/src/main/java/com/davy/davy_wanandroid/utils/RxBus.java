package com.davy.davy_wanandroid.utils;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * author: Davy
 * date: 2018/9/27
 */
public class RxBus {

    private final FlowableProcessor<Object> mBus;

    private RxBus(){
        mBus = PublishProcessor.create().toSerialized();
    }

    public static RxBus getDefault(){
        return RxBusHolder.INSTANCE;
    }

    private static class RxBusHolder{
       private static final RxBus INSTANCE = new RxBus();
    }

    /**
     * 提供一个新事物
     * @param o
     */
    public void post(Object o){
        mBus.onNext(o);
    }

    /**
     *根据传递的evenType返回特定的evenType类型的被观察者
     * @param evenType
     * @param <T>
     * @return Flowable<T>
     */
    public <T> Flowable<T> toFlowable (Class<T> evenType){
        return mBus.ofType(evenType);
    }
}
