package com.davy.davy_wanandroid.base.activity;

import android.support.v7.app.AppCompatDelegate;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.utils.CommonUtils;

import javax.inject.Inject;


/**
 * author: Davy
 * date: 18/9/19
 */
public abstract class BaseActivity<T extends AbstractPresenter> extends AbstractSimpleActivity implements AbstractView {
    @Inject
    protected T mPresenter;

    @Override
    protected void onViewCreated() {
        if(mPresenter != null){
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        if(mPresenter != null){
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void useNightMode(boolean isNightMode) {
        if(isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
    }

    @Override
    public void showSnackBar(String msg) {
        CommonUtils.showSnackMessage(this,msg);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showErrorMsg(String msg) {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showNormal() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void showLoginView() {

    }

    @Override
    public void showLoginOutView() {

    }
}
