package com.davy.davy_wanandroid.core.http.api;

import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.LoginData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * author: Davy
 * date: 18/9/17
 */
public interface WanAndroidApi {

    String HOST = "http://www.wanandroid.com/";

    /**
     * 注册
     *
     * @param username
     * @param password
     * @param repassword
     * @return 注册数据
     */
    @POST("user/register")
    @FormUrlEncoded
    Observable<BaseResponse<LoginData>> getRegisterData(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword );

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return 登录数据
     */
    @POST("user/login")
    @FormUrlEncoded
    Observable<BaseResponse<LoginData>> getLoginData(@Field("username") String username, @Field("password") String password);
}
