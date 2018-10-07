package com.davy.davy_wanandroid.ui.knowledgehierarchy.fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.fragment.BaseRootFragment;
import com.davy.davy_wanandroid.bean.knowledgehierarchy.KnowledgeHierarchyData;
import com.davy.davy_wanandroid.contract.KnowledgeHierarchy.KnowledgeHierarchyContract;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.KnowledgeHierarchy.KnowledgeHierarchyPresenter;
import com.davy.davy_wanandroid.ui.knowledgehierarchy.activity.KnowledgeHierarchyDetailActivity;
import com.davy.davy_wanandroid.ui.knowledgehierarchy.adapter.KnowledgeHierarchyAdapter;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Davy
 * date: 18/9/29
 */
public class KnowledgeHierarchyFragment extends BaseRootFragment<KnowledgeHierarchyPresenter> implements KnowledgeHierarchyContract.View {

    @BindView(R.id.knowledge_hierarchy_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;

    private List<KnowledgeHierarchyData> mKnowledgeHierarchyDataList;
    private KnowledgeHierarchyAdapter mHierarchyAdapter;
    private boolean isRefresh;

    public static KnowledgeHierarchyFragment getInstans(String param1, String param2) {
        KnowledgeHierarchyFragment fragment = new KnowledgeHierarchyFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_knowledge_hierarchy;
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

    private void initRecyclerView() {
        mKnowledgeHierarchyDataList = new ArrayList<>();
        mHierarchyAdapter = new KnowledgeHierarchyAdapter(R.layout.item_knowledge_hierarchy, mKnowledgeHierarchyDataList);
        mHierarchyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startDetailPager(view, position);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mHierarchyAdapter);
    }

    private void startDetailPager(View view, int position) {
        if(mHierarchyAdapter.getData().size() <= 0 || mHierarchyAdapter.getData().size() <= position){
            return;
        }
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(_mActivity, view, getString(R.string.share_view));
        Intent intent = new Intent(_mActivity, KnowledgeHierarchyDetailActivity.class);
        intent.putExtra(Constants.ARG_PARAM1, mHierarchyAdapter.getData().get(position));
        if(modleFiltering()){
            startActivity(intent, options.toBundle());
        }else{
            startActivity(intent);
        }
    }

    //机型适配
    private boolean modleFiltering() {
        return !Build.MANUFACTURER.contains(Constants.SAMSUNG) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        setRefresh();
        mPresenter.getKnowledgeHierarchyData(true);
        if(CommonUtils.isNetWorkConnected()){
            showLoading();
        }
    }

    @Override
    public void showKnowledgeHierarchyData(List<KnowledgeHierarchyData> knowledgeHierarchyDataList) {
        if(mPresenter.getCurrentPage() == 1){
            mRecyclerView.setVisibility(View.VISIBLE);
        }else {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
        if(mHierarchyAdapter.getData().size() < knowledgeHierarchyDataList.size()){
            mKnowledgeHierarchyDataList = knowledgeHierarchyDataList;
            mHierarchyAdapter.replaceData(knowledgeHierarchyDataList);
        }else {
            if(!isRefresh){
                CommonUtils.showSnackMessage(_mActivity, getString(R.string.load_more_no_data));
            }
        }
        showNormal();
    }

    @Override
    public void showError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        super.showError();
    }

    @Override
    public void reload() {
        if(mPresenter != null && mRecyclerView.getVisibility()== View.INVISIBLE){
            mPresenter.getKnowledgeHierarchyData(false);
        }
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
                mPresenter.getKnowledgeHierarchyData(false);
                refreshLayout.finishRefresh(1000);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                isRefresh = false;
                mPresenter.getKnowledgeHierarchyData(false);
                refreshLayout.finishLoadMore(1000);
            }
        });
    }

}
