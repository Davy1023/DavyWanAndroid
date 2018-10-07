package com.davy.davy_wanandroid.ui.knowledgehierarchy.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.activity.BaseActivity;
import com.davy.davy_wanandroid.base.fragment.BaseFragment;
import com.davy.davy_wanandroid.bean.knowledgehierarchy.KnowledgeHierarchyData;
import com.davy.davy_wanandroid.contract.KnowledgeHierarchy.KnowledgeHierarchyDetailContract;
import com.davy.davy_wanandroid.core.event.KnowledgeJumpTopEvent;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.KnowledgeHierarchy.KnowledgeHierarchyDetailPresenter;
import com.davy.davy_wanandroid.ui.knowledgehierarchy.fragment.KnowledgeHierarchyListFragment;
import com.davy.davy_wanandroid.utils.RxBus;
import com.davy.davy_wanandroid.utils.StatusBarUtli;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: Davy
 * date: 18/10/5
 */
public class KnowledgeHierarchyDetailActivity extends BaseActivity<KnowledgeHierarchyDetailPresenter> implements KnowledgeHierarchyDetailContract.View {

    @BindView(R.id.common_toolbar_title_tv)
    TextView mToolbarTitle;
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.knowledge_hierarchy_detail_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.knowledge_hierarchy_detail_viewpager)
    ViewPager mViewpager;
    @BindView(R.id.knowledge_floating_action_btn)
    FloatingActionButton mFloatingActionBtn;

    private List<KnowledgeHierarchyData> mKnowledgeHierarchyDataList;
    private List<BaseFragment> mFragments = new ArrayList<>();
    private String mChapterName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_knowledge_hierarchy_detail;
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
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(false);
        StatusBarUtli.setStatusColor(getWindow(), ContextCompat.getColor(this, R.color.main_status_bar_blue), 1f);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressedSupport();
            }
        });

        if(getIntent().getBooleanExtra(Constants.IS_SINGLE_CHAPTER, false)){
            startSingleKnowledgePager();
        }else{
            startKnowledgeListPager();
        }
    }

    //多个知识列表体系页面
    private void startKnowledgeListPager() {
        KnowledgeHierarchyData knowledgeHierarchyData = (KnowledgeHierarchyData) getIntent().getSerializableExtra(Constants.ARG_PARAM1);
        if(knowledgeHierarchyData == null || knowledgeHierarchyData.getName() == null){
            return;
        }
        mToolbarTitle.setText(knowledgeHierarchyData.getName());
        mKnowledgeHierarchyDataList = knowledgeHierarchyData.getChildren();
        if(mKnowledgeHierarchyDataList == null){
            return;
        }
        for (KnowledgeHierarchyData data : mKnowledgeHierarchyDataList){
            mFragments.add(KnowledgeHierarchyListFragment.getInstance(data.getId(), null));
        }
    }

    //首页tag进入的单个列表知识体系页面
    private void startSingleKnowledgePager(){
        String superChapterName = getIntent().getStringExtra(Constants.SUPER_CHAPTER_NAME);
        mChapterName = getIntent().getStringExtra(Constants.CHAPTER_NAME);
        int chapterId = getIntent().getIntExtra(Constants.CHAPTER_ID, 0);
        mToolbarTitle.setText(superChapterName);
        mFragments.add(KnowledgeHierarchyListFragment.getInstance(chapterId, null));
    }

    @Override
    protected void initEventAndData() {
        initViewPagerAndTabLayout();
    }

    private void initViewPagerAndTabLayout() {
        mViewpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments == null? 0 : mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if(getIntent().getBooleanExtra(Constants.IS_SINGLE_CHAPTER, false)){
                    return mChapterName;
                }else{
                    return mKnowledgeHierarchyDataList.get(position).getName();
                }
            }
        });

        mTabLayout.setViewPager(mViewpager);
    }

    @OnClick({R.id.knowledge_floating_action_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.knowledge_floating_action_btn:
                RxBus.getDefault().post(new KnowledgeJumpTopEvent());
                break;
             default:
                break;
        }
    }

    @Override
    public void showSwithNavitation() {
        onBackPressedSupport();
    }

}
