package com.davy.davy_wanandroid.base.presenter;

import com.davy.davy_wanandroid.base.view.AbstractView;

import io.reactivex.disposables.Disposable;

/**
 * Presenter基类
 *
 * author: Davy
 * date: 18/9/15
 */
public interface AbstractPresenter<T extends AbstractView> {

    /**
     * 注入view
     * @param view
     */
    void attachView(T view);

    /**
     * 回收view
     */
    void detachView();

    /**
     * 添加事件订阅
     * @param disposable
     */
    void addRxBindingSubscribe(Disposable disposable);

    boolean getNightModeState();

    /**
     * 保存登录状态
     * @param isLogin
     */
    void setLoginStatus(boolean isLogin);

    /**
     * 保存登录账号
     * @param account
     */
    void setLoginAccount(String account);

    /**
     * 保存登录密码
     * @param password
     */
    void setLoginPassword(String password);

    /**
     * 获取登录状态
     * @return LoginStatus
     */
    boolean getLoginStatus();

    /**
     * 获取登录账号
     * @return login account
     */
    String getLoginAccount();





    int getCurrentPage();
}
