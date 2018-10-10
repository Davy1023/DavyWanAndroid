package com.davy.davy_wanandroid.presenter.main;

import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.contract.main.SettingContract;
import com.davy.davy_wanandroid.core.DataManager;

import javax.inject.Inject;

/**
 * author: Davy
 * date: 2018/10/10
 */
public class SettingPresenter extends BasePresenter<SettingContract.View> implements SettingContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public SettingPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public boolean getAutoCacheState() {
        return mDataManager.getAutoCacheState();
    }

    @Override
    public boolean getNightModeState() {
        return mDataManager.getNightModeState();
    }

    @Override
    public void setAutoCacheState(boolean b) {
        mDataManager.setAutoCacheState(b);
    }

    @Override
    public void setNightModeState(boolean b) {
        mDataManager.setNightModeState(b);
    }
}
