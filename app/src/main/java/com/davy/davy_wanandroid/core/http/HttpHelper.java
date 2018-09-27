package com.davy.davy_wanandroid.core.http;

import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.LoginData;

import io.reactivex.Observable;

/**
 * author: Davy
 * date: 2018/9/26
 */
public interface HttpHelper {
    /**
     * @param username
     * @param password
     * @param repassword
     * @return 注册数据
     */
    Observable<BaseResponse<LoginData>> getRegisterData(String username, String password, String repassword);

    /**
     * @param username
     * @param password
     * @return 登录数据
     */
    Observable<BaseResponse<LoginData>> getLoginData(String username, String password);
}
