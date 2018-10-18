package com.davy.davy_wanandroid.core;

import com.davy.davy_wanandroid.bean.BaseGankResponse;
import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.girls.GirlsImageData;
import com.davy.davy_wanandroid.bean.knowledgehierarchy.KnowledgeHierarchyData;
import com.davy.davy_wanandroid.bean.main.BannerData;
import com.davy.davy_wanandroid.bean.main.LoginData;
import com.davy.davy_wanandroid.bean.main.TopSearchData;
import com.davy.davy_wanandroid.bean.main.UsefulSiteData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;
import com.davy.davy_wanandroid.bean.navigation.NavigationListData;
import com.davy.davy_wanandroid.core.dao.HistoryData;
import com.davy.davy_wanandroid.core.db.DbHelper;
import com.davy.davy_wanandroid.core.http.HttpHelper;
import com.davy.davy_wanandroid.core.prefs.PreferencesHelper;

import java.util.List;

import io.reactivex.Observable;

/**
 * author: Davy
 * date: 2018/9/26
 */
public class DataManager implements HttpHelper, PreferencesHelper, DbHelper{
    private HttpHelper mHttpHelper;
    private PreferencesHelper mPreferencesHelper;
    private DbHelper mDbHelper;

    public DataManager(HttpHelper httpHelper, PreferencesHelper preferencesHelper, DbHelper dbHelper){
        this.mHttpHelper = httpHelper;
        this.mPreferencesHelper = preferencesHelper;
        this.mDbHelper = dbHelper;
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
    public Observable<BaseGankResponse<List<GirlsImageData>>> getGirlsListData(String type, int count, int pageIndex) {
        return mHttpHelper.getGirlsListData(type, count, pageIndex);
    }

    @Override
    public Observable<BaseResponse<List<UsefulSiteData>>> getUsefulSiteData() {
        return mHttpHelper.getUsefulSiteData();
    }

    @Override
    public Observable<BaseResponse<WanAndroidArticleListData>> getSearchList(int pageNum, String k) {
        return mHttpHelper.getSearchList(pageNum, k);
    }

    @Override
    public Observable<BaseResponse<List<TopSearchData>>> getTopSearchData() {
        return mHttpHelper.getTopSearchData();
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
    public void setAutoCacheState(boolean b) {
        mPreferencesHelper.setAutoCacheState(b);
    }

    @Override
    public boolean getNightModeState() {
        return mPreferencesHelper.getNightModeState();
    }

    @Override
    public List<HistoryData> addHistoryData(String data) {
        return mDbHelper.addHistoryData(data);
    }

    @Override
    public void clearnHistoryData() {
        mDbHelper.clearnHistoryData();
    }

    @Override
    public List<HistoryData> getAllHistoryData() {
        return mDbHelper.getAllHistoryData();
    }
}
