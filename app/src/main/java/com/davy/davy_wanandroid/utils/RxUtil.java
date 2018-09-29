package com.davy.davy_wanandroid.utils;

import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.core.http.exception.OtherException;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author: Davy
 * date: 2018/9/26
 */
public class RxUtil {
    /**
     * 统一线程处理
     * @param <T> 指定的泛型类型
     * @return FlowableTransformer
     */
    public static <T>FlowableTransformer<T,T> rxflSchedulerHelper(){
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    /**
     * 统一线程处理
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<T,T> rxScheduleHelper(){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一结果处理
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<BaseResponse<T>, T> handleResult(){
        return new ObservableTransformer<BaseResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseResponse<T>> responseObservable) {
                return responseObservable.flatMap(new Function<BaseResponse<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(BaseResponse<T> baseResponse) throws Exception {
                        if(baseResponse.getErrorCode() == BaseResponse.SUCCESS && baseResponse.getData() != null
                                && CommonUtils.isNetWorkConnected()){
                            return createData(baseResponse.getData());
                        }else {
                            return Observable.error(new OtherException());
                        }

                    }
                });
            }
        };
    }

    /**
     * 得到 Observable
     * @param <T>
     * @return Observable
     */
    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }
}
