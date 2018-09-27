package com.davy.davy_wanandroid.contract.main;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;

/**
 * author: Davy
 * date: 2018/9/27
 */
public interface LoginContract {

    interface View extends AbstractView{
        /**
         * 登录成功视图
         */
        void showLoginSuccess();
    }

    interface Presenter extends AbstractPresenter<LoginContract.View>{
        /**
         *  获取登录数据
         * @param username
         * @param password
         */
        void getLoginData(String username, String password);
    }
}
