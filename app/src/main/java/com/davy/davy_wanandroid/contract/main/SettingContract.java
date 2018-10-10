package com.davy.davy_wanandroid.contract.main;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;

/**
 * author: Davy
 * date: 2018/10/10
 */
public interface SettingContract {

    interface View extends AbstractView{

    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 获取自动缓存状态
         * @return
         */
        boolean getAutoCacheState();

        /**
         * 设置自动缓存状态
         * @param b
         */
        void setAutoCacheState(boolean b);

        /**
         * 设置夜间模式状态
         * @param b
         */
        void setNightModeState(boolean b);
    }
}
