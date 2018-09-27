package com.davy.davy_wanandroid.core.event;

/**
 * author: Davy
 * date: 2018/9/27
 */
public class LoginEvent {
    private boolean isLogin;
    public LoginEvent(boolean isLogin){
        this.isLogin = isLogin;
    }

    public boolean isLogin(){
        return isLogin;
    }

    public void setIsLogin(boolean isLogin){
        this.isLogin = isLogin;
    }
}
