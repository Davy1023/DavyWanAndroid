package com.davy.davy_wanandroid.contract.KnowledgeHierarchy;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;

/**
 * author: Davy
 * date: 18/10/6
 */
public interface KnowledgeHierarchyListContract {

    interface View extends AbstractView{

        /**
         * 显示知识体系详细列表数据
         * @param wanAndroidArticleListData
         */
        void showKnowledgeHierarchyDetailData(WanAndroidArticleListData wanAndroidArticleListData);

        /**
         * 文章收藏视图
         * @param position
         * @param wanAndroidArticleData
         * @param wanAndroidArticleListData
         */
        void showCollectArticleData(int position, WanAndroidArticleData wanAndroidArticleData, WanAndroidArticleListData wanAndroidArticleListData);

        /**
         * 文章收藏取消视图
         * @param position
         * @param wanAndroidArticleData
         * @param wanAndroidArticleListData
         */
        void showCancelArticleData(int position, WanAndroidArticleData wanAndroidArticleData, WanAndroidArticleListData wanAndroidArticleListData);

        void showJumpTheTop();

        void showReloadDetailEvent();
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 获取知识体系详细列表数据
         * @param page
         * @param id
         * @param isShowError
         */
        void getKnowledgeHierarchyDetailData(int page, int id, boolean isShowError);

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
