package com.davy.davy_wanandroid.core.http;

import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.BannerData;
import com.davy.davy_wanandroid.bean.main.LoginData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;

import java.util.List;

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

    /**
     * 轮播图
     * @return 轮播数据
     */
    Observable<BaseResponse<List<BannerData>>> getBannerData();

    /**
     * 获取文章列表
     * @param pageNum 页数
     * @return 文章数据
     */
    Observable<BaseResponse<WanAndroidArticleListData>> getWanAndroidArticleListData(int pageNum);

    /**
     * 收藏站内文章
     * @param id 文章id
     * @return 站内文章id数据
     */
    Observable<BaseResponse<WanAndroidArticleListData>> addCollectArticle(int id);

    /**
     * 取消站内收藏文章
     * @param id 文章id
     * @return 站内文章id数据
     */
    Observable<BaseResponse<WanAndroidArticleListData>> cancelCollectArticle(int id);
 }
