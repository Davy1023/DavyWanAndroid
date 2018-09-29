package com.davy.davy_wanandroid.core.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.app.WanAndroidApplication;

import javax.inject.Inject;

/**
 * author: Davy
 * date: 2018/9/27
 */
public class PreferencesHelperImpl implements PreferencesHelper {

    private SharedPreferences mSharedPreferences;

    @Inject
    public PreferencesHelperImpl(){
        mSharedPreferences = WanAndroidApplication.getInstance().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void setLoginStatus(boolean isLogin) {
        mSharedPreferences.edit().putBoolean(Constants.LOGIN_STATUS,isLogin);
    }

    @Override
    public void setLoginAccount(String account) {
        mSharedPreferences.edit().putString(Constants.ACCOUNT,account).apply();
    }

    @Override
    public void setLoginPassword(String password) {
        mSharedPreferences.edit().putString(Constants.PASSWORD,password).apply();
    }

    @Override
    public void setCurrentPage(int position) {
        mSharedPreferences.edit().putInt(Constants.Current_PAGE,position).apply();
    }

    @Override
    public void setNightModeState(boolean state) {
        mSharedPreferences.edit().putBoolean(Constants.NIGHT_MODE_STATE,state).apply();
    }
}
