package com.davy.davy_wanandroid.core;

import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.knowledgehierarchy.KnowledgeHierarchyData;
import com.davy.davy_wanandroid.bean.main.BannerData;
import com.davy.davy_wanandroid.bean.main.LoginData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;
import com.davy.davy_wanandroid.bean.navigation.NavigationListData;
import com.davy.davy_wanandroid.core.http.HttpHelper;
import com.davy.davy_wanandroid.core.prefs.PreferencesHelper;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: Davy
 * date: 2018/9/26
 */
public class DataManager implements HttpHelper,PreferencesHelper {
    private HttpHelper mHttpHelper;
    private PreferencesHelper mPreferencesHelper;

    public DataManager(HttpHelper httpHelper,PreferencesHelper preferencesHelper){
        this.mHttpHelper = httpHelper;
        this.mPreferencesHelper = preferencesHelper;
    }
    @Override
    public Observable<BaseResponse<LoginData>> getRegisterData(String username, String password, String repassword) {
        return mHttpHelper.getRegisterData(username,password,repassword);
    }

    @Override
    public Observable<BaseResponse<LoginData>> getLoginData(String username, String password) {
        return mHttpHelper.getLoginData(username,password);
    }

    @Override
    public Observable<BaseResponse<List<BannerData>>> getBannerData() {
        return mHttpHelper.getBannerData();
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> getWanAndroidArticleListData(int pageNum) {
        return mHttpHelper.getWanAndroidArticleListData(pageNum);
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> addCollectArticle(int id) {
        return mHttpHelper.addCollectArticle(id);
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> cancelCollectArticle(int id) {
        return mHttpHelper.cancelCollectArticle(id);
    }

    @Override
    public Observable<BaseResponse<List<KnowledgeHierarchyData>>> getKnowledgeHierarchyData() {
        return mHttpHelper.getKnowledgeHierarchyData();
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> getKnowledgeHierarchyDetailData(int page, int id) {
        return mHttpHelper.getKnowledgeHierarchyDetailData(page, id);
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> cancelCollectPageArticle(int id) {
        return mHttpHelper.cancelCollectPageArticle(id);
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> getCollectArticleList(int page) {
        return mHttpHelper.getCollectArticleList(page);
    }

    @Override
    public Observable<BaseResponse<List<NavigationListData>>> getNavigationListData() {
        return mHttpHelper.getNavigationListData();
    }

    @Override
    public void setLoginStatus(boolean isLogin) {
        mPreferencesHelper.setLoginStatus(isLogin);
    }

    @Override
    public void setLoginAccount(String account) {
        mPreferencesHelper.setLoginAccount(account);
    }

    @Override
    public void setLoginPassword(String password) {
        mPreferencesHelper.setLoginPassword(password);
    }

    @Override
    public void setCurrentPage(int page) {
        mPreferencesHelper.setCurrentPage(page);
    }

    @Override
    public int getCurrentPage() {
        return mPreferencesHelper.getCurrentPage();
    }

    @Override
    public void setNightModeState(boolean state) {
        mPreferencesHelper.setNightModeState(state);
    }

    @Override
    public boolean getLoginStatus() {
        return mPreferencesHelper.getLoginStatus();
    }

    @Override
    public String getLoginAccount() {
        return mPreferencesHelper.getLoginAccount();
    }

    @Override
    public String getLoginPassword() {
        return mPreferencesHelper.getLoginPassword();
    }

    @Override
    public boolean getAutoCacheState() {
        return mPreferencesHelper.getAutoCacheState();
    }

    @Override
    public boolean getNoImageState() {
        return mPreferencesHelper.getNoImageState();
    }

    @Override
    public void setAutoCacheState(boolean b) {
        mPreferencesHelper.setAutoCacheState(b);
    }

    @Override
    public void setNoImageState(boolean b) {
        mPreferencesHelper.setNoImageState(b);
    }
}
