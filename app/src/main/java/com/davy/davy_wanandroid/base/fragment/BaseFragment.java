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
    T mPresenter;

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
    public void showSnackBar(String msg) {
        CommonUtils.showSnackMessage(_mActivity,msg);
    }
}
