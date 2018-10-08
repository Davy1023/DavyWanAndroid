package com.davy.davy_wanandroid.contract.navigation;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.bean.navigation.NavigationListData;

import java.util.List;

/**
 * author: Davy
 * date: 18/10/8
 */
public interface NavigationContract {

    interface View extends AbstractView{

        /**
         * show navigationListData
         * @param navigationListData
         */
        void showNavigationListaData(List<NavigationListData> navigationListData);
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 获取导航列表数据
         * @param isShowError
         */
        void getNavigationListData(boolean isShowError);
    }
}
