package com.davy.davy_wanandroid.core.prefs;

/**
 * author: Davy
 * date: 2018/9/27
 */
public interface PreferencesHelper {
    /**
     * 记录登录状态
     * @param isLogin
     */
    void setLoginStatus(boolean isLogin);
    /**
     * 记录登录账号
     * @param account
     */
    void setLoginAccount(String account);

    /**
     * 记录登录密码
     * @param password
     */
    void setLoginPassword(String password);
}
