package com.davy.davy_wanandroid.base.presenter;

import com.davy.davy_wanandroid.base.view.AbstractView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 管理事件订阅的生命周期
 *
 * author: Davy
 * date: 18/9/15
 */
public class BasePresenter<T extends AbstractView> implements AbstractPresenter<T>{

    protected T mView;
    private CompositeDisposable mCompositeDisposable;

    public BasePresenter(){

    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        if(mCompositeDisposable!=null){
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void addRxBindingSubscribe(Disposable disposable) {
            addSubscribe(disposable);
    }

    @Override
    public boolean getNightModeState() {
        return false;
    }

    @Override
    public int getCurrentPage() {
        return 0;
    }

    protected void addSubscribe(Disposable disposable) {
        if(mCompositeDisposable == null){
            mCompositeDisposable = new CompositeDisposable();
        }
            mCompositeDisposable.add(disposable);
    }
}
