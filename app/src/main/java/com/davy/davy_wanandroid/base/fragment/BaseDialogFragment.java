package com.davy.davy_wanandroid.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.utils.CommonUtils;

import javax.inject.Inject;

/**
 * author: Davy
 * date: 2018/10/15
 */
public abstract class BaseDialogFragment<T extends AbstractPresenter> extends AbstractSimpleDialogFragment implements AbstractView {

    @Inject
    protected T mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initInjector(WanAndroidApplication.getInstance().getApplicationComponent());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void showLoading() {

    }

    @Override
    public void showNormal() {

    }

    @Override
    public void showErrorMsg(String msg) {
        if (getActivity() != null){
            CommonUtils.showSnackMessage(getActivity(), msg);
        }
    }

    @Override
    public void showError() {

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

    @Override
    public void showCollectSucces() {

    }

    @Override
    public void showCancleCollectSucces() {

    }

    @Override
    public void useNightMode(boolean isNightMode) {

    }

    @Override
    public void showSnackBar(String msg) {
        if(getActivity() == null){
            return;
        }
        CommonUtils.showSnackMessage(getActivity(), msg);
    }
}
