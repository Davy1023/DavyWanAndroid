package com.davy.davy_wanandroid.ui.main.fragment;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.fragment.BaseRootFragment;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;
import com.davy.davy_wanandroid.contract.main.CollectContract;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.main.CollectPresenter;
import com.davy.davy_wanandroid.ui.mainpager.adapter.ArticleListAdapter;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.davy.davy_wanandroid.utils.KnowledgeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: Davy
 * date: 18/9/29
 */
public class CollectFragment extends BaseRootFragment<CollectPresenter> implements CollectContract.View {

    @BindView(R.id.collect_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.collect_floating_action_btn)
    FloatingActionButton mFloatingActionBtn;

    private List<WanAndroidArticleData> mArticleData;
    private ArticleListAdapter mArticleListAdapter;
    private boolean isRefresh = true;
    private int mCurrentPage;

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
        return R.layout.fragment_collect;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        showRefreshEvent();
    }

    @Override
    protected void initView() {
        super.initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mArticleData = new ArrayList<>();
        mArticleListAdapter = new ArticleListAdapter(R.layout.item_pager, mArticleData);
        mArticleListAdapter.isColletPage();
        mArticleListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startDetailPager(view, position);
            }
        });
        mArticleListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                clickChildEvent(view, position);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mArticleListAdapter);
    }

    private void clickChildEvent(View view, int position) {
        switch (view.getId()){
            case R.id.item_pager_chapterName:
                startSingleKnowledgePager(position);
                break;
            case R.id.item_pager_like_iv:
                cancelCollet(position);
                break;
            default:
                break;
        }
    }
    private void cancelCollet(int position){
        if(mArticleListAdapter.getData().size() <= 0 || mArticleListAdapter.getData().size() <= position){
            return;
        }
        mPresenter.cancelCollectPageArticle(position, mArticleListAdapter.getData().get(position));
    }

    private void startSingleKnowledgePager(int position) {
        if(mArticleListAdapter.getData().size() <= 0 || mArticleListAdapter.getData().size() <= position){
            return;
        }
        KnowledgeUtils.startKnowledgeHierarchyDetailActivity(_mActivity,
                true,
                mArticleListAdapter.getData().get(position).getChapterName(),
                mArticleListAdapter.getData().get(position).getChapterName(),
                mArticleListAdapter.getData().get(position).getChapterId()
                );
    }

    private void startDetailPager(View view, int position) {
        if(mArticleListAdapter.getData().size() <= 0 || mArticleListAdapter.getData().size() <= position){
            return;
        }
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(_mActivity, view, getString(R.string.share_view));
        KnowledgeUtils.startArticleDetailActivity(_mActivity,options,
                mArticleListAdapter.getData().get(position).getId(),
                mArticleListAdapter.getData().get(position).getTitle(),
                mArticleListAdapter.getData().get(position).getLink(),
                true,
                true,
                false
                );
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mPresenter.getCollectArticleList(mCurrentPage, true);
        setRefresh();
        if(CommonUtils.isNetWorkConnected()){
            showLoading();
        }
    }

    @OnClick({R.id.collect_floating_action_btn})
    void Onclick(View view){
        switch (view.getId()){
            case R.id.collect_floating_action_btn:
                mRecyclerView.smoothScrollToPosition(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void reload() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void showCollectArticleList(WanAndroidArticleListData wanAndroidArticleListData) {
        if(mArticleListAdapter == null){
            return;
        }
        mArticleData = wanAndroidArticleListData.getDatas();
        if(isRefresh){
           mArticleListAdapter.replaceData(mArticleData);
        }else{
            showLodeMore(wanAndroidArticleListData);
        }
        if(mArticleListAdapter.getData().size() == 0){
            CommonUtils.showSnackMessage(_mActivity, getString(R.string.no_collect));
        }

        showNormal();
    }

    private void showLodeMore(WanAndroidArticleListData wanAndroidArticleListData) {
        if(mArticleData.size() > 0){
            mArticleData.addAll(wanAndroidArticleListData.getDatas());
            mArticleListAdapter.addData(wanAndroidArticleListData.getDatas());
        }else{
            if(mArticleListAdapter.getData().size() != 0){
                CommonUtils.showSnackMessage(_mActivity, getString(R.string.load_more_no_data));
            }
        }
    }

    @Override
    public void showCancelCollectPageArticle(int position, WanAndroidArticleData wanAndroidArticleData, WanAndroidArticleListData wanAndroidArticleListData) {
        mArticleListAdapter.remove(position);
        CommonUtils.showSnackMessage(_mActivity, getString(R.string.cancel_collet_success));
    }

    @Override
    public void showRefreshEvent() {
        if(isAdded()){
            mCurrentPage = 0;
            isRefresh = true;
            mPresenter.getCollectArticleList(mCurrentPage, false);
        }
    }

    private void setRefresh() {
        mRefreshLayout.setPrimaryColorsId(Constants.BLUE_COLOR, R.color.white);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                showRefreshEvent();
                refreshLayout.finishRefresh(1000);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                mCurrentPage ++ ;
                isRefresh = false;
                mPresenter.getCollectArticleList(mCurrentPage, false);
                refreshLayout.finishLoadMore(1000);
            }
        });
    }
}
