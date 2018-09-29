package com.davy.davy_wanandroid.contract.main;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;

/**
 * author: Davy
 * date: 2018/9/29
 */
public interface MainContract {

    interface View extends AbstractView{

        void showSwitchNavigation();

        void showAutoLoginView();
    }

    interface Presenter extends AbstractPresenter<View>{
        /**
         * 设置page
         * @param page
         */
        void setCurrentPage(int page);

        /**
         * 设置夜间模式状态
         * @param state
         */
        void setNightModeState(boolean state);
    }
}
