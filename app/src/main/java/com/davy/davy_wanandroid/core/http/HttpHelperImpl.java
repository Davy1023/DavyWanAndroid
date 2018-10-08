package com.davy.davy_wanandroid.core.http;

import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.knowledgehierarchy.KnowledgeHierarchyData;
import com.davy.davy_wanandroid.bean.main.BannerData;
import com.davy.davy_wanandroid.bean.main.LoginData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;
import com.davy.davy_wanandroid.bean.navigation.NavigationListData;
import com.davy.davy_wanandroid.core.http.api.WanAndroidApi;

import java.util.List;

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
        mWanAndroidApi = wanAndroidApi;
    }

    @Override
    public Observable<BaseResponse<LoginData>> getRegisterData(String username, String password, String repassword) {
        return mWanAndroidApi.getRegisterData(username, password, repassword);
    }

    @Override
    public Observable<BaseResponse<LoginData>> getLoginData(String username, String password) {
        return mWanAndroidApi.getLoginData(username, password);
    }

    @Override
    public Observable<BaseResponse<List<BannerData>>> getBannerData() {
        return mWanAndroidApi.getBannerData();
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> getWanAndroidArticleListData(int pageNum) {
        return mWanAndroidApi.getWanAndroidArticleListData(pageNum);
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> addCollectArticle(int id) {
        return mWanAndroidApi.addCollectArticle(id);
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> cancelCollectArticle(int id) {
        return mWanAndroidApi.cancelCollectArticle(id, -1);
    }

    @Override
    public Observable<BaseResponse<List<KnowledgeHierarchyData>>> getKnowledgeHierarchyData() {
        return mWanAndroidApi.getKnowledgeHierarchyData();
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> getKnowledgeHierarchyDetailData(int page, int id) {
        return mWanAndroidApi.getKnowledgeHierarchyDetailData(page, id);
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> cancelCollectPageArticle(int id) {
        return mWanAndroidApi.cancelCollectPageArticle(id, -1);
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> getCollectArticleList(int page) {
        return mWanAndroidApi.getCollectArticleList(page);
    }

    @Override
    public Observable<BaseResponse<List<NavigationListData>>> getNavigationListData() {
        return getNavigationListData();
    }
}
