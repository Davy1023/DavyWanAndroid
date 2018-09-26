package com.davy.davy_wanandroid.core;

import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.LoginData;
import com.davy.davy_wanandroid.core.http.HttpHelper;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * author: Davy
 * date: 2018/9/26
 */
public class DataManager implements HttpHelper {
    private HttpHelper mHttpHelper;

    public DataManager(HttpHelper httpHelper){
        this.mHttpHelper = httpHelper;
    }
    @Override
    public Observable<BaseResponse<LoginData>> getRegisterData(String username, String password, String repassword) {
        return mHttpHelper.getRegisterData(username,password,repassword);
    }
}
