package com.davy.davy_wanandroid.contract.KnowledgeHierarchy;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * author: Davy
 * date: 18/10/6
 */
public interface ArticleDetailContract {

    interface View extends AbstractView{

        /**
         * 收藏文章视图
         * @param wanAndroidArticleListData
         */
        void showCollectArticleData(WanAndroidArticleListData wanAndroidArticleListData);

        /**
         * 取消收藏文章视图
         * @param wanAndroidArticleListData
         */
        void showCancelCollectAtrticleData(WanAndroidArticleListData wanAndroidArticleListData);

        /**
         * share event
         */
        void shareEvent();

        /**
         * share error
         */
        void shareError();
    }

    interface Presenter extends AbstractPresenter<View>{

        boolean getAutoCacheState();

        boolean getNoImageState();

        /**
         * 收藏文章
         * @param id
         */
        void addCollectArtitle(int id);

        /**
         * 取消文章
         * @param id
         */
        void cancelCollectArticle(int id);

        /**
         * 取消收藏页面的文章
         * @param id
         */
        void cancelCollectPageArticle(int id);

        /**
         * 权限验证
         * @param rxPermissions
         */
        void shareEventPermissionVerify(RxPermissions rxPermissions);
    }
}
