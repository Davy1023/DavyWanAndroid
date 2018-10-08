package com.davy.davy_wanandroid.presenter.main;

import android.text.TextUtils;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.base.rx.BaseObsever;
import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.LoginData;
import com.davy.davy_wanandroid.contract.main.LoginContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.core.event.LoginEvent;
import com.davy.davy_wanandroid.utils.LogHelper;
import com.davy.davy_wanandroid.utils.RxBus;
import com.davy.davy_wanandroid.utils.RxUtil;

import javax.inject.Inject;

/**
 * author: Davy
 * date: 2018/9/27
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public LoginPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void getLoginData(String username, String password) {
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
           mView.showSnackBar(WanAndroidApplication.getInstance().getString(R.string.account_password_username_null));
        }
        addSubscribe(mDataManager.getLoginData(username,password)
                .compose(RxUtil.<BaseResponse<LoginData>>rxScheduleHelper())
                .compose(RxUtil.<LoginData>handleResult())
                .subscribeWith(new BaseObsever<LoginData>(mView, WanAndroidApplication.getInstance().getString(R.string.login_fail)) {
                    @Override
                    public void onNext(LoginData loginData) {

                        setLoginAccount(loginData.getUsername());
                        setLoginPassword(loginData.getPassword());
                        setLoginStatus(true);
                        RxBus.getDefault().post(new LoginEvent(true));

                        mView.showLoginSuccess();

                      LogHelper.e("loginData" + loginData);
                    }
                })

        );

    }
}
