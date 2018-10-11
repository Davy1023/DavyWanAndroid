package com.davy.davy_wanandroid.ui.girls.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.fragment.BaseRootFragment;
import com.davy.davy_wanandroid.bean.girls.GirlsImageData;
import com.davy.davy_wanandroid.contract.girls.GirlsContract;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.girls.GirlsPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;

/**
 * author: Davy
 * date: 18/9/29
 */
public class GrilsFragment extends BaseRootFragment<GirlsPresenter> implements GirlsContract.View {

    @BindView(R.id.girls_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;

    public static GrilsFragment getInstance(String param1, String param2) {
        GrilsFragment fragment = new GrilsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_girls;
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

    }

    @Override
    public void showGirlsListData(List<GirlsImageData> girlsImageDataList) {

    }
}
