package com.davy.davy_wanandroid.ui.main.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.activity.BaseRootActivity;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;
import com.davy.davy_wanandroid.contract.main.SearchListContract;
import com.davy.davy_wanandroid.core.event.SwitchNavigatinEvent;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.main.SearchListPresenter;
import com.davy.davy_wanandroid.ui.mainpager.adapter.ArticleListAdapter;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.davy.davy_wanandroid.utils.KnowledgeUtils;
import com.davy.davy_wanandroid.utils.RxBus;
import com.davy.davy_wanandroid.utils.StatusBarUtli;
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
 * date: 18/10/21
 */
public class SearchListActivity extends BaseRootActivity<SearchListPresenter> implements SearchListContract.View {

    @BindView(R.id.common_toolbar_title_tv)
    TextView mToolTitle;
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.normal_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_list_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.search_list_floating_action_btn)
    FloatingActionButton mFloatingActionBtn;

    private String mSearchText;
    private int mCurrentPage;
    private boolean isAddData;
    private List<WanAndroidArticleData> mArticleDataList;
    private ArticleListAdapter mArticleListAdapter;
    private int articlePosition;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_list;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);
    }

    @Override
    protected void initToolbar() {
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
         mSearchText = (String) bundle.get(Constants.SEARCH_TEXT);
        if(!TextUtils.isEmpty(mSearchText)){
            mToolTitle.setText(mSearchText);
        }

        StatusBarUtli.setStatusColor(getWindow(), ContextCompat.getColor(this, R.color.search_status_bar_white), 1f);
        if(mPresenter.getNightModeState()){
            mToolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.blue_gradient_bg));
            setToolbarView(R.color.white, R.drawable.ic_arrow_back_white_24dp);
        }else{
            StatusBarUtli.setStatusDarkColor(getWindow());
            mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            setToolbarView(R.color.title_black, R.drawable.ic_arrow_back_grey_24dp);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressedSupport();
            }
        });
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mPresenter.getSearchList(mCurrentPage, mSearchText, true);
        initRecyclerView();
        setRefresh();
        if(CommonUtils.isNetWorkConnected()){
            showLoading();
        }
    }

    @Override
    public void reload() {
        if(mPresenter != null){
            mPresenter.getSearchList(mCurrentPage, mSearchText, false);
        }
    }

    private void initRecyclerView() {
        mArticleDataList = new ArrayList<>();
        mArticleListAdapter = new ArticleListAdapter(R.layout.item_pager, mArticleDataList);
        mArticleListAdapter.isSerchPage();
        mArticleListAdapter.isNightMode(mPresenter.getNightModeState());
        mArticleListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startArticleDetailPager(view, position);
            }
        });
        mArticleListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                clickChildrenEvent(view, position);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mArticleListAdapter);
    }

    @OnClick({R.id.search_list_floating_action_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.search_list_floating_action_btn:
                mRecyclerView.smoothScrollToPosition(0);
                break;
            default:
                break;
        }
    }
    private void clickChildrenEvent(View view, int position) {
        switch (view.getId()){
            case R.id.item_pager_chapterName:
                startSingleChapterKnowledgePager(position);
                break;
            case  R.id.item_pager_like_iv:
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
            onBackPressedSupport();
            RxBus.getDefault().post(new SwitchNavigatinEvent());
        }
    }

    private void likeEvent(int position) {
        if(!mPresenter.getLoginStatus()){
            startActivity(new Intent(this, LoginActivity.class));
            CommonUtils.showSnackMessage(this, getString(R.string.login_tint));
            return;
        }
        if(mArticleListAdapter.getData().size() <= 0 || mArticleListAdapter.getData().size() <= position){
            return;
        }
        if(mArticleListAdapter.getData().get(position).isCollect()){
            mPresenter.cancelCollectArticle(position, mArticleListAdapter.getData().get(position));
        }else{
            mPresenter.addCollectArticle(position, mArticleListAdapter.getData().get(position));
        }
    }

    private void startSingleChapterKnowledgePager(int position) {
        if(mArticleListAdapter.getData().size() <= 0 || mArticleListAdapter.getData().size() <= position){
            return;
        }
        KnowledgeUtils.startKnowledgeHierarchyDetailActivity(this,
                true,
                mArticleListAdapter.getData().get(position).getSuperChapterName(),
                mArticleListAdapter.getData().get(position).getChapterName(),
                mArticleListAdapter.getData().get(position).getChapterId());
    }

    private void startArticleDetailPager(View view, int position) {
        if(mArticleListAdapter.getData().size() <=0 || mArticleListAdapter.getData().size() <= position){
            return;
        }
        articlePosition = position;
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view, getString(R.string.share_view));
        KnowledgeUtils.startArticleDetailActivity(this,
                options,
                mArticleListAdapter.getData().get(position).getId(),
                mArticleListAdapter.getData().get(position).getTitle(),
                mArticleListAdapter.getData().get(position).getLink(),
                mArticleListAdapter.getData().get(position).isCollect(),
                false,
                false
                );
    }

    @Override
    public void showSearchList(WanAndroidArticleListData wanAndroidArticleListData) {
        mArticleDataList = wanAndroidArticleListData.getDatas();
        if(isAddData){
            if(mArticleDataList.size() > 0){
                mArticleListAdapter.addData(mArticleDataList);
            }else{
                CommonUtils.showMessage(this, getString(R.string.load_more_no_data));
            }
        }else{
            mArticleListAdapter.replaceData(mArticleDataList);
        }
        showNormal();
    }

    @Override
    public void showCollectArticleData(int position, WanAndroidArticleData wanAndroidArticleData, WanAndroidArticleListData wanAndroidArticleListData) {
        mArticleListAdapter.setData(position, wanAndroidArticleData);
        CommonUtils.showSnackMessage(this, getString(R.string.collet_success));
    }

    @Override
    public void showCancelArticleData(int position, WanAndroidArticleData wanAndroidArticleData, WanAndroidArticleListData wanAndroidArticleListData) {
        mArticleListAdapter.setData(position, wanAndroidArticleData);
        CommonUtils.showSnackMessage(this, getString(R.string.cancel_collet_success));
    }

    @Override
    public void showCollectSucces() {
        showCollectResult(true);
    }


    @Override
    public void showCancleCollectSucces() {
        showCollectResult(false);
    }

    private void showCollectResult(boolean collectResult) {
        if(mArticleListAdapter.getData().size() > articlePosition){
            mArticleListAdapter.getData().get(articlePosition).setCollect(collectResult);
            mArticleListAdapter.setData(articlePosition, mArticleListAdapter.getData().get(articlePosition));
        }
    }

    private void setToolbarView(int textColor, int navigationIcon) {
        mToolTitle.setTextColor(ContextCompat.getColor(this, textColor));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, navigationIcon));
    }

    private void setRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mCurrentPage = 0;
                isAddData = false;
                mPresenter.getSearchList(0, mSearchText, false);
                refreshLayout.finishRefresh(1000);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                mCurrentPage ++;
                isAddData = true;
                mPresenter.getSearchList(mCurrentPage, mSearchText, false);
                refreshLayout.finishLoadMore(1000);
            }
        });
    }
}
