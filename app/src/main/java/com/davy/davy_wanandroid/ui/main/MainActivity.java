package com.davy.davy_wanandroid.ui.main;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.base.activity.BaseActivity;
import com.davy.davy_wanandroid.contract.main.MainContract;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.presenter.main.MainPresenter;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View{


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {

    }

    @Override
    public void showSwitchNavigation() {

    }

    @Override
    public void showAutoLoginView() {

    }

    @Override
    public void showLoginView() {

    }

    @Override
    public void showLoginOutView() {

    }
}
