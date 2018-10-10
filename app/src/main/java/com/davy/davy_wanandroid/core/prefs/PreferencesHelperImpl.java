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
        mSharedPreferences.edit().putBoolean(Constants.LOGIN_STATUS,isLogin).apply();
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
        mSharedPreferences.edit().putInt(Constants.CURRENT_PAGE,position).apply();
    }

    @Override
    public int getCurrentPage() {
        return mSharedPreferences.getInt(Constants.CURRENT_PAGE, 0);
    }

    @Override
    public void setNightModeState(boolean state) {
        mSharedPreferences.edit().putBoolean(Constants.NIGHT_MODE_STATE,state).apply();
    }

    @Override
    public boolean getNightModeState() {
        return mSharedPreferences.getBoolean(Constants.NIGHT_MODE_STATE, false);
    }

    @Override
    public boolean getLoginStatus() {
        return mSharedPreferences.getBoolean(Constants.LOGIN_STATUS,false);
    }

    @Override
    public String getLoginAccount() {
        return mSharedPreferences.getString(Constants.ACCOUNT,"");
    }

    @Override
    public String getLoginPassword() {
        return mSharedPreferences.getString(Constants.PASSWORD, "");
    }

    @Override
    public boolean getAutoCacheState() {
        return mSharedPreferences.getBoolean(Constants.AUTO_CACHE_STATE, true);
    }

    @Override
    public void setAutoCacheState(boolean b) {
        mSharedPreferences.edit().putBoolean(Constants.AUTO_CACHE_STATE, b).apply();
    }
}
