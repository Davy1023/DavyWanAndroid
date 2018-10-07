package com.davy.davy_wanandroid.ui.knowledgehierarchy.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.fragment.BaseRootFragment;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;
import com.davy.davy_wanandroid.contract.KnowledgeHierarchy.KnowledgeHierarchyListContract;
import com.davy.davy_wanandroid.core.event.CollectEvent;
import com.davy.davy_wanandroid.core.event.SwitchNavigatinEvent;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.KnowledgeHierarchy.KnowledgeHierarchyListPresenter;
import com.davy.davy_wanandroid.ui.main.activity.LoginActivity;
import com.davy.davy_wanandroid.ui.mainpager.adapter.ArticleListAdapter;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.davy.davy_wanandroid.utils.KnowledgeUtils;
import com.davy.davy_wanandroid.utils.RxBus;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

/**
 * author: Davy
 * date: 18/10/5
 */
public class KnowledgeHierarchyListFragment extends BaseRootFragment<KnowledgeHierarchyListPresenter> implements KnowledgeHierarchyListContract.View {

    @BindView(R.id.knowledge_hierarchy_list_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.normal_view)
    SmartRefreshLayout mRefreshLayout;

    private ArticleListAdapter mArticleListAdapter;
    private int id;
    private int mCurrentPager;
    private boolean isRefresh = true;
    private int mArticlePosition;

    public static KnowledgeHierarchyListFragment getInstance(int id, String param2) {
        KnowledgeHierarchyListFragment fragment = new KnowledgeHierarchyListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_PARAM1, id);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_knowledge_hierarchy_list;
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
        isInnerFragment = true;
        setRefresh();
        Bundle bundle = getArguments();
        id = bundle.getInt(Constants.ARG_PARAM1, 0);
        if(id == 0){
            return;
        }
        mCurrentPager = 0;
        mPresenter.getKnowledgeHierarchyDetailData(mCurrentPager, id, true);
        if(CommonUtils.isNetWorkConnected()){
            showLoading();
        }

    }

    @Override
    public void reload() {
        if(mPresenter != null){
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void showKnowledgeHierarchyDetailData(WanAndroidArticleListData wanAndroidArticleListData) {
        if(isRefresh){
            mArticleListAdapter.replaceData(wanAndroidArticleListData.getDatas());
        }else {
            if(wanAndroidArticleListData.getDatas().size() > 0){
                mArticleListAdapter.addData(wanAndroidArticleListData.getDatas());
            }else {
                CommonUtils.showSnackMessage(_mActivity, getString(R.string.load_more_no_data));
            }
        }
        showNormal();
    }

    @Override
    public void showCollectArticleData(int position, WanAndroidArticleData wanAndroidArticleData, WanAndroidArticleListData wanAndroidArticleListData) {
        mArticleListAdapter.addData(position, wanAndroidArticleData);
        RxBus.getDefault().post(new CollectEvent(false));
        CommonUtils.showSnackMessage(_mActivity, getString(R.string.collet_success));
    }

    @Override
    public void showCancelArticleData(int position, WanAndroidArticleData wanAndroidArticleData, WanAndroidArticleListData wanAndroidArticleListData) {
        mArticleListAdapter.addData(position, wanAndroidArticleData);
        RxBus.getDefault().post(new CollectEvent(true));
        CommonUtils.showSnackMessage(_mActivity, getString(R.string.cancel_collet_success));
    }

    @Override
    public void showJumpTheTop() {
        if(mRecyclerView != null){
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void showReloadDetailEvent() {
        if(mRefreshLayout != null){
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void showCollectSucces() {
        if(mArticleListAdapter != null && mArticleListAdapter.getData().size() > mArticlePosition){
            mArticleListAdapter.getData().get(mArticlePosition).setCollect(true);
            mArticleListAdapter.setData(mArticlePosition, mArticleListAdapter.getData().get(mArticlePosition));
        }
    }

    @Override
    public void showCancleCollectSucces() {
        if(mArticleListAdapter != null && mArticleListAdapter.getData().size() > mArticlePosition){
            mArticleListAdapter.getData().get(mArticlePosition).setCollect(false);
            mArticleListAdapter.setData(mArticlePosition, mArticleListAdapter.getData().get(mArticlePosition));
        }
    }

    private void initRecyclerView() {
        mArticleListAdapter = new ArticleListAdapter(R.layout.item_pager, null);
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
        switch(view.getId()){
            case R.id.item_pager_chapterName:
                break;
            case R.id.item_pager_like_iv:
                likeEvent(position);
                break;
            case R.id.item_pager_tag_red_tv:
                clickTag(position);
                break;
             default:
                 break;
        }
    }

    private void clickTag(int position) {
        if(mArticleListAdapter.getData().size() <= 0 || mArticleListAdapter.getData().size() <= position){
            return;
        }
        String superChapterName = mArticleListAdapter.getData().get(position).getSuperChapterName();
        if(superChapterName.contains(getString(R.string.navigation))){
            RxBus.getDefault().post(new SwitchNavigatinEvent());
        }
    }

    private void likeEvent(int position) {
        if(mArticleListAdapter.getData().size() <= 0 || mArticleListAdapter.getData().size() <= position){
            return;
        }
        if(!mPresenter.getLoginStatus()){
            startActivity(new Intent(_mActivity, LoginActivity.class));
            CommonUtils.showSnackMessage(_mActivity,getString(R.string.login_tint));
        }
        if(mArticleListAdapter.getData().get(position).isCollect()){
            mPresenter.cancelCollectArticle(position, mArticleListAdapter.getData().get(position));
        }else{
            mPresenter.addCollectArticle(position, mArticleListAdapter.getData().get(position));
        }
    }

    private void startDetailPager(View view, int position) {
        if(mArticleListAdapter.getData().size() <= 0 || mArticleListAdapter.getData().size() <= position){
            return;
        }
        mArticlePosition = position;
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(_mActivity, view, getString(R.string.share_view));
        KnowledgeUtils.startArticleDetailActivity(_mActivity,
                options,
                mArticleListAdapter.getData().get(position).getId(),
                mArticleListAdapter.getData().get(position).getTitle().trim(),
                mArticleListAdapter.getData().get(position).getLink().trim(),
                mArticleListAdapter.getData().get(position).isCollect(),
                false,
                false);
    }

    private void setRefresh() {
        mRefreshLayout.setPrimaryColorsId(Constants.BLUE_COLOR, R.color.white);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mCurrentPager = 0;
                if(id != 0){
                    isRefresh = true;
                    mPresenter.getKnowledgeHierarchyDetailData(0, id, false);
                }
                refreshLayout.finishRefresh(1000);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                mCurrentPager ++;
                if(id != 0){
                    isRefresh = false;
                    mPresenter.getKnowledgeHierarchyDetailData(mCurrentPager, id, false);
                }
                refreshLayout.finishLoadMore(1000);
            }
        });
    }
}
