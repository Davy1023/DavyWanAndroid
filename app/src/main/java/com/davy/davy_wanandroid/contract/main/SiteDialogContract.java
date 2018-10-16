package com.davy.davy_wanandroid.contract.main;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.bean.main.UsefulSiteData;

import java.util.List;

/**
 * author: Davy
 * date: 2018/10/15
 */
public interface SiteDialogContract {

    interface View extends AbstractView{

        /**
         * 常用网站
         * @param siteDataList
         */
        void showUsefulSiteData(List<UsefulSiteData> siteDataList);
    }

    interface Presenter extends AbstractPresenter<SiteDialogContract.View>{

        /**
         * 获取常用网站数据
         */
        void getUsefulSiteData();
    }
}
