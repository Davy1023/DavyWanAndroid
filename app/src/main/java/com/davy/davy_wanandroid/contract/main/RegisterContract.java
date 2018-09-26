package com.davy.davy_wanandroid.contract.main;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;

/**
 * author: Davy
 * date: 2018/9/26
 */
public interface RegisterContract {

    interface View extends AbstractView{

        void showRegisterSuccess();
    }

    interface presenter extends AbstractPresenter<RegisterContract.View>{
        /**
         *
         * @param username
         * @param password
         * @param repassword
         */
        void getRegisterData(String username, String password, String repassword);
    }
}
