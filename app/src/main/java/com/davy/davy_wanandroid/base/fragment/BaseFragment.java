package com.davy.davy_wanandroid.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.utils.CommonUtils;

import javax.inject.Inject;

/**
 * author: Davy
 * date: 18/9/19
 */
public abstract class BaseFragment<T extends AbstractPresenter> extends AbstactSimpleFragment implements AbstractView {

    @Inject
    protected T mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mPresenter != null){
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onDestroyView() {
        if(mPresenter != null){
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroyView();
    }

    @Override
    public void useNightMode(boolean isNightMode) {

    }

    @Override
    public void showCollectSucces() {

    }

    @Override
    public void showCancleCollectSucces() {

    }

    @Override
    public void showLoginView() {

    }

    @Override
    public void showLoginOutView() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showNormal() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void showErrorMsg(String msg) {
        if(isAdded()){
            CommonUtils.showSnackMessage(_mActivity, msg);
        }
    }

    @Override
    public void showSnackBar(String msg) {
        CommonUtils.showSnackMessage(_mActivity,msg);
    }
}
