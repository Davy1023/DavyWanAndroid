package com.davy.davy_wanandroid.core;

import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.LoginData;
import com.davy.davy_wanandroid.core.http.HttpHelper;
import com.davy.davy_wanandroid.core.prefs.PreferencesHelper;

import io.reactivex.Observable;

/**
 * author: Davy
 * date: 2018/9/26
 */
public class DataManager implements HttpHelper,PreferencesHelper {
    private HttpHelper mHttpHelper;
    private PreferencesHelper mPreferencesHelper;

    public DataManager(HttpHelper httpHelper,PreferencesHelper preferencesHelper){
        this.mHttpHelper = httpHelper;
        this.mPreferencesHelper = preferencesHelper;
    }
    @Override
    public Observable<BaseResponse<LoginData>> getRegisterData(String username, String password, String repassword) {
        return mHttpHelper.getRegisterData(username,password,repassword);
    }

    @Override
    public Observable<BaseResponse<LoginData>> getLoginData(String username, String password) {
        return mHttpHelper.getLoginData(username,password);
    }

    @Override
    public void setLoginStatus(boolean isLogin) {
        mPreferencesHelper.setLoginStatus(isLogin);
    }

    @Override
    public void setLoginAccount(String account) {
        mPreferencesHelper.setLoginAccount(account);
    }

    @Override
    public void setLoginPassword(String password) {
        mPreferencesHelper.setLoginPassword(password);
    }

    @Override
    public void setCurrentPage(int page) {
        mPreferencesHelper.setCurrentPage(page);
    }

    @Override
    public void setNightModeState(boolean state) {
        mPreferencesHelper.setNightModeState(state);
    }

    @Override
    public boolean getLoginStatus() {
        return mPreferencesHelper.getLoginStatus();
    }

    @Override
    public String getLoginAccount() {
        return mPreferencesHelper.getLoginAccount();
    }
}
