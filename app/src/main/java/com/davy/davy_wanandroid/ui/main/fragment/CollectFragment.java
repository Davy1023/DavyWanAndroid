package com.davy.davy_wanandroid.ui.main.fragment;

import android.os.Bundle;

import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.fragment.BaseRootFragment;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;

/**
 * author: Davy
 * date: 18/9/29
 */
public class CollectFragment extends BaseRootFragment {

    public static CollectFragment getInstance(String param1, String param2) {
        CollectFragment fragment = new CollectFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {

    }

    @Override
    public void showErrorMsg(String msg) {

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
    public void useNightMode(boolean isNightMode) {

    }
}
