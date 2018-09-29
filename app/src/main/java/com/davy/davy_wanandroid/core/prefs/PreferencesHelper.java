package com.davy.davy_wanandroid.core.prefs;

/**
 * author: Davy
 * date: 2018/9/27
 */
public interface PreferencesHelper {

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
     * 保存当前page
     * @param page
     */
    void setCurrentPage(int page);

    /**
     * 保存夜间模式状态
     * @param state
     */
    void setNightModeState(boolean state);

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
}
