package com.davy.davy_wanandroid.ui.girls.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.fragment.BaseRootFragment;
import com.davy.davy_wanandroid.bean.girls.GirlsImageData;
import com.davy.davy_wanandroid.contract.girls.GirlsContract;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.girls.GirlsPresenter;
import com.davy.davy_wanandroid.ui.girls.activity.GirlsImageBrowserActivity;
import com.davy.davy_wanandroid.ui.girls.adapter.GirlsAdapter;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author: Davy
 * date: 18/9/29
 */
public class GirlsFragment extends BaseRootFragment<GirlsPresenter> implements GirlsContract.View {

    @BindView(R.id.girls_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;

    private int mCurrentPage = 1;
    private int mPageSize = 20;
    private boolean isRefresh = true;
    private GirlsAdapter mGirlsAdapter;
    private List<GirlsImageData> mGirlsListData = new ArrayList<>();
    private ArrayList<String> mImageList = new ArrayList<>();

    public static GirlsFragment getInstance(String param1, String param2) {
        GirlsFragment fragment = new GirlsFragment();
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
        super.initView();
        initRecyclerView();
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        setRefresh();
        mPresenter.getGirlsListData(Constants.FULI, mPageSize, 1, true);
        if(CommonUtils.isNetWorkConnected()){
            showLoading();
        }

    }

    @Override
    public void showError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        super.showError();
    }

    @Override
    public void reload() {
        if(mPresenter != null && mRecyclerView.getVisibility() == View.INVISIBLE){
            mPresenter.getGirlsListData(Constants.FULI, 1, mPageSize, false);
        }
    }

    @Override
    public void showGirlsListData(List<GirlsImageData> girlsImageDataList) {
        if(isRefresh){
            mGirlsListData = girlsImageDataList;
            mGirlsAdapter.replaceData(girlsImageDataList);
        }else{
            if(girlsImageDataList.size() > 0){
                mGirlsListData.addAll(girlsImageDataList);
                mGirlsAdapter.addData(girlsImageDataList);
            }else{
                CommonUtils.showSnackMessage(_mActivity, getString(R.string.load_more_no_data));
            }
        }
            showNormal();
    }

    private void initRecyclerView() {
        mGirlsAdapter = new GirlsAdapter(R.layout.item_girls_image, null);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mGirlsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startGirlsImageBrowser(view, position);
            }
        });
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mGirlsAdapter);
    }

    private void startGirlsImageBrowser(View view, int position) {
        mImageList.clear();
        for(int i = 0 ; i < mGirlsListData.size() ; i++){
            mImageList.add(mGirlsListData.get(i).getUrl());
        }
        Intent intent = new Intent(_mActivity, GirlsImageBrowserActivity.class);
        intent.putExtra(Constants.IMAGELIST, mImageList);
        intent.putExtra(Constants.CURRENT_POSITION, position);

        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view,
                view.getWidth() / 2,
                view.getHeight() / 2,
                0,
                0);
        _mActivity.startActivity(intent, options.toBundle());
    }

    public void jumpToTop(){
        if(mRecyclerView != null){
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    private void setRefresh() {
        mRefreshLayout.setPrimaryColorsId(Constants.BLUE_COLOR, R.color.white);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                isRefresh = true;
                mCurrentPage = 1;
                mPresenter.getGirlsListData(Constants.FULI, mPageSize, 1, false);
                refreshLayout.finishRefresh(1000);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                isRefresh = false;
                mCurrentPage ++;
                mPresenter.getGirlsListData(Constants.FULI, mPageSize, mCurrentPage, false);
                refreshLayout.finishLoadMore(1000);
            }
        });
    }

}
