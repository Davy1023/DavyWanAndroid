package com.davy.davy_wanandroid.contract.main;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;

/**
 * author: Davy
 * date: 18/10/21
 */
public interface SearchListContract {

    interface View extends AbstractView{

        /**
         * show searchList
         * @param wanAndroidArticleListData
         */
        void showSearchList(WanAndroidArticleListData wanAndroidArticleListData);

        /**
         * show collect articleData
         * @param position
         * @param wanAndroidArticleData
         * @param wanAndroidArticleListData
         */
        void showCollectArticleData(int position, WanAndroidArticleData wanAndroidArticleData, WanAndroidArticleListData wanAndroidArticleListData);

        /**
         * show cancel articleData
         * @param position
         * @param wanAndroidArticleData
         * @param wanAndroidArticleListData
         */
        void showCancelArticleData(int position, WanAndroidArticleData wanAndroidArticleData, WanAndroidArticleListData wanAndroidArticleListData);
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 获取搜索列表数据
         * @param page
         * @param k
         * @param isShowError
         */
        void getSearchList(int page, String k, boolean isShowError);

        /**
         * 添加文章收藏
         * @param position
         * @param wanAndroidArticleData
         */
        void addCollectArticle(int position, WanAndroidArticleData wanAndroidArticleData);

        /**
         * 取消文章收藏
         * @param position
         * @param wanAndroidArticleData
         */
        void cancelCollectArticle(int position, WanAndroidArticleData wanAndroidArticleData);
    }
}
