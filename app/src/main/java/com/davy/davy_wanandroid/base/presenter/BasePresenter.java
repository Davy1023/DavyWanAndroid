package com.davy.davy_wanandroid.base.presenter;

import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.core.DataManager;

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
    private DataManager mDataManager;

    public BasePresenter(DataManager dataManager){
        this.mDataManager = dataManager;
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
    public void setLoginStatus(boolean isLogin) {
        mDataManager.setLoginStatus(isLogin);
    }

    @Override
    public void setLoginAccount(String account) {
        mDataManager.setLoginAccount(account);
    }

    @Override
    public void setLoginPassword(String password) {
        mDataManager.setLoginPassword(password);
    }

    @Override
    public boolean getLoginStatus() {
        return mDataManager.getLoginStatus();
    }

    @Override
    public String getLoginAccount() {
        return mDataManager.getLoginAccount();
    }

    @Override
    public int getCurrentPage() {
        return mDataManager.getCurrentPage();
    }

    protected void addSubscribe(Disposable disposable) {
        if(mCompositeDisposable == null){
            mCompositeDisposable = new CompositeDisposable();
        }
            mCompositeDisposable.add(disposable);
    }
}
