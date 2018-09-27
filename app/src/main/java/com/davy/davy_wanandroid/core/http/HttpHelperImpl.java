package com.davy.davy_wanandroid.core.http;

import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.LoginData;
import com.davy.davy_wanandroid.core.http.api.WanAndroidApi;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: Davy
 * date: 2018/9/26
 */
public class HttpHelperImpl implements HttpHelper {
    private WanAndroidApi mWanAndroidApi;

    @Inject
    public HttpHelperImpl(WanAndroidApi wanAndroidApi){
        this.mWanAndroidApi = wanAndroidApi;
    }

    @Override
    public Observable<BaseResponse<LoginData>> getRegisterData(String username, String password, String repassword) {
        return mWanAndroidApi.getRegisterData(username,password,repassword);
    }

    @Override
    public Observable<BaseResponse<LoginData>> getLoginData(String username, String password) {
        return mWanAndroidApi.getLoginData(username,password);
    }
}
