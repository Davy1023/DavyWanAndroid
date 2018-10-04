package com.davy.davy_wanandroid.contract.mainpager;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.bean.main.BannerData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;

import java.util.List;

/**
 * author: Davy
 * date: 18/10/1
 */
public interface MainPagerContract {

    interface View extends AbstractView{
        /**
         * 自动登录成功
         */
        void showAutoLoginSuccess();

        /**
         * 自动登录失败
         */
        void showAutoLoginFail();

        /**
         * 文章内容
         * @param wanAndroidArticleListData
         * @param isRefresh
         */
        void showArticleList(WanAndroidArticleListData wanAndroidArticleListData, boolean isRefresh);

        /**
         * 收藏的文章
         * @param positin
         * @param wanAndroidArticleData
         * @param wanAndroidArticleListData
         */
        void showCollectArticleData(int positin, WanAndroidArticleData wanAndroidArticleData, WanAndroidArticleListData wanAndroidArticleListData);

        /**
         * 取消收藏的文章
         * @param position
         * @param wanAndroidArticleData
         * @param wanAndroidArticleListData
         */
        void showCancleCollectArticleData(int position, WanAndroidArticleData wanAndroidArticleData, WanAndroidArticleListData wanAndroidArticleListData);

        /**
         * 轮播图数据
         * @param bannerDataList
         */
        void showBannerData(List<BannerData> bannerDataList);
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 获取登录密码
         * @return login password
         */
        String getLoginPassword();

        /**
         * 加载MainPager首页数据
         */
        void loadMainPagerData();

        /**
         * 获取文章数据
         * @param isShowError
         */
        void getWanAndroidArticleList(boolean isShowError);

        /**
         * 加载更多数据
         */
        void loadMoreData();

        /**
         * 收藏文章
         * @param position
         * @param wanAndroidArticleData
         */
        void addCollectArticle(int position, WanAndroidArticleData wanAndroidArticleData);

        /**
         * 取消收藏文章
         * @param position
         * @param wanAndroidArticleData
         */
        void cancelCollectArticle(int position, WanAndroidArticleData wanAndroidArticleData);

        /**
         * 加载轮播数据
         * @param isShowError
         */
        void getBannerData(boolean isShowError);

        /**
         * 自动刷新
         * @param isShowError
         */
        void autoRefresh(boolean isShowError);

        /**
         * 加载更多
         */
        void loadMore();

    }
}
