package com.davy.davy_wanandroid.ui.navigation.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.fragment.BaseRootFragment;
import com.davy.davy_wanandroid.bean.navigation.NavigationListData;
import com.davy.davy_wanandroid.contract.navigation.NavigationContract;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.navigation.NavigationPresenter;
import com.davy.davy_wanandroid.utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import q.rorbin.verticaltablayout.VerticalTabLayout;

/**
 * author: Davy
 * date: 18/9/29
 */
public class NavigationFragment extends BaseRootFragment<NavigationPresenter> implements NavigationContract.View {

    @BindView(R.id.navigation_tab_layout)
    VerticalTabLayout mVerticalTabLayout;
    @BindView(R.id.navigation_divider)
    View mDivider;
    @BindView(R.id.navigation_RecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.normal_view)
    LinearLayout mNormalView;

    public static NavigationFragment getInstance(String param1, String param2) {
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_navigation;
    }


    @Override
    public void initInjector(ApplicationComponent applicationComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);
    }

    @Override
    protected void initView() {
        super.initView();
        initRecyclerView();
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mPresenter.getNavigationListData(true);
        if(CommonUtils.isNetWorkConnected()){
            showLoading();
        }
    }

    @Override
    public void showErrorMsg(String msg) {

    }

    @Override
    public void reload() {

    }

    @Override
    public void showNavigationListaData(List<NavigationListData> navigationListData) {

    }

    private void initRecyclerView() {
    }

}
