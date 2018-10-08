package com.davy.davy_wanandroid.contract.main;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;

/**
 * author: Davy
 * date: 2018/10/8
 */
public interface CollectContract {

    interface View extends AbstractView{
        /**
         * 收藏列表视图
         * @param wanAndroidArticleListData
         */
        void showCollectArticleList(WanAndroidArticleListData wanAndroidArticleListData);

        /**
         * 取消收藏视图
         * @param position
         * @param wanAndroidArticleData
         * @param wanAndroidArticleListData
         */
        void showCancelCollectPageArticle(int position, WanAndroidArticleData wanAndroidArticleData, WanAndroidArticleListData wanAndroidArticleListData);

        /**
         * show refresh event
         */
        void showRefreshEvent();
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 获取收藏列表数据
         * @param page
         * @param isShowError
         */
        void getCollectArticleList(int page, boolean isShowError);

        /**
         * 取消收藏列表中的文字
         * @param position
         * @param wanAndroidArticleData
         */
        void cancelCollectPageArticle(int position, WanAndroidArticleData wanAndroidArticleData);
    }
}
