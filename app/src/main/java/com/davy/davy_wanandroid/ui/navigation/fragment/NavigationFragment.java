package com.davy.davy_wanandroid.ui.navigation.fragment;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
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
import com.davy.davy_wanandroid.ui.navigation.adapter.NavigationAdapter;
import com.davy.davy_wanandroid.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

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

    private NavigationAdapter mNavigationAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean needScroll;
    private int index;
    private boolean isClickTab;

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
    public void showError() {
        mVerticalTabLayout.setVisibility(View.INVISIBLE);
        mNormalView.setVisibility(View.INVISIBLE);
        mDivider.setVisibility(View.INVISIBLE);
        super.showError();
    }

    @Override
    public void reload() {
        if(mPresenter != null && mNormalView.getVisibility() == View.INVISIBLE){
            mPresenter.getNavigationListData(false);
        }
    }

    @Override
    public void showNavigationListaData(final List<NavigationListData> navigationListData) {
        mVerticalTabLayout.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return navigationListData == null ? 0 : navigationListData.size();
            }

            @Override
            public ITabView.TabBadge getBadge(int position) {
                return null;
            }

            @Override
            public ITabView.TabIcon getIcon(int position) {
                return null;
            }

            @Override
            public ITabView.TabTitle getTitle(int position) {
                return new TabView.TabTitle.Builder()
                        .setContent(navigationListData.get(position).getName())
                        .setTextColor(ContextCompat.getColor(_mActivity, R.color.colorPrimary),
                                ContextCompat.getColor(_mActivity, R.color.black ))
                        .build();
            }

            @Override
            public int getBackground(int position) {
                return -1;
            }
        });

        if(mPresenter.getCurrentPage() == Constants.TYPE_NAVIGATION){
            setChilderVisibility(View.VISIBLE);
        }else {
            setChilderVisibility(View.INVISIBLE);
        }
        mNavigationAdapter.replaceData(navigationListData);
        leftLinkageRight();
        showNormal();
    }

    private void leftLinkageRight() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(needScroll && (newState == RecyclerView.SCROLL_STATE_IDLE)){
                    scrollRecyclerView();
                }
                rightLinkageLeft(newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(needScroll){
                    scrollRecyclerView();
                }
            }
        });

        mVerticalTabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                isClickTab = true;
                selectTag(position);
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
    }

    private void selectTag(int position) {
        index = position;
        mRecyclerView.stopScroll();
        smoothScrollToPosition(position);
    }

    private void smoothScrollToPosition(int currentPosition) {
        int firstPosition = mLayoutManager.findFirstVisibleItemPosition();
        int lastPosition = mLayoutManager.findLastVisibleItemPosition();
        if(currentPosition <= firstPosition){
            mRecyclerView.smoothScrollToPosition(currentPosition);
        }else if(currentPosition <= lastPosition){
            int top = mRecyclerView.getChildAt(currentPosition - firstPosition).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        }else {
            mRecyclerView.smoothScrollToPosition(currentPosition);
            needScroll = true;
        }
    }

    private void rightLinkageLeft(int newState) {
        if(newState == RecyclerView.SCROLL_STATE_IDLE){
            if(isClickTab){
                isClickTab = false;
                return;
            }
            int firstPosition = mLayoutManager.findFirstVisibleItemPosition();
            if(index != firstPosition){
                index = firstPosition;
                setChecked(index);
            }
        }
    }

    private void setChecked(int position) {
        if(isClickTab){
            isClickTab = false;
        }else {
            if(mVerticalTabLayout == null){
                return;
            }
            mVerticalTabLayout.setTabSelected(index);
        }

        index = position;
    }

    private void scrollRecyclerView() {
        needScroll = false;
        int indexDstance = index - mLayoutManager.findFirstVisibleItemPosition();
        if(indexDstance >= 0 && indexDstance < mRecyclerView.getChildCount()){
            int top = mRecyclerView.getChildAt(indexDstance).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        }
    }

    private void setChilderVisibility(int visible) {
        mVerticalTabLayout.setVisibility(visible);
        mNormalView.setVisibility(visible);
        mDivider.setVisibility(visible);
    }

    private void initRecyclerView() {
        List<NavigationListData> navigationListData = new ArrayList<>();
        mNavigationAdapter = new NavigationAdapter(R.layout.item_navigation, navigationListData);
        mLayoutManager = new LinearLayoutManager(_mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mNavigationAdapter);
    }

    public void jumpToTop(){
        if(mVerticalTabLayout != null){
            mVerticalTabLayout.setTabSelected(0);
        }
    }
}
