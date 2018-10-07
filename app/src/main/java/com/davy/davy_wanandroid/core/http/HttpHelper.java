package com.davy.davy_wanandroid.core.http;

import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.knowledgehierarchy.KnowledgeHierarchyData;
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
     * 文章列表
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

    /**
     * 知识体系
     * @return 知识体系列表数据
     */
    Observable<BaseResponse<List<KnowledgeHierarchyData>>> getKnowledgeHierarchyData();

    /**
     * 知识体系下的详细文章列表
     * @param page 页数
     * @param id 文章id
     * @return 详细文章列表数据
     */
    Observable<BaseResponse<WanAndroidArticleListData>> getKnowledgeHierarchyDetailData(int page, int id);

    /**
     * 取消收藏页面的文章
     * @param id
     * @return 收藏列表数据
     */
    Observable<BaseResponse<WanAndroidArticleListData>> cancelCollectPageArticle(int id);
 }
