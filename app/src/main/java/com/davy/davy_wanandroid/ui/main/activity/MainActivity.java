package com.davy.davy_wanandroid.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.activity.BaseActivity;
import com.davy.davy_wanandroid.base.fragment.BaseFragment;
import com.davy.davy_wanandroid.contract.main.MainContract;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.main.MainPresenter;
import com.davy.davy_wanandroid.ui.girls.fragment.GrilsFragment;
import com.davy.davy_wanandroid.ui.knowledge.fragment.KnowledgeHierarchyFragment;
import com.davy.davy_wanandroid.ui.main.fragment.CollectFragment;
import com.davy.davy_wanandroid.ui.main.fragment.MainPagerFragment;
import com.davy.davy_wanandroid.ui.main.fragment.SettingFragment;
import com.davy.davy_wanandroid.ui.navigation.fragment.NavigationFragment;
import com.davy.davy_wanandroid.utils.StatusBarUtli;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {


    @BindView(R.id.common_toolbar_title_tv)
    TextView mToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fragment_group)
    FrameLayout fragmentGroup;
    @BindView(R.id.main_floating_action_btn)
    FloatingActionButton mFloatingActionBtn;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private ArrayList<BaseFragment> mFragments;
    private MainPagerFragment mMainPagerFragment;
    private KnowledgeHierarchyFragment mKnowledgeHierarchyFragment;
    private NavigationFragment mNavigationFragment;
    private GrilsFragment mGrilsFragment;
    private TextView mUserTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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
        mToolbarTitleTv.setText(R.string.home_pager);
        StatusBarUtli.setStatusColor(getWindow(), ContextCompat.getColor(this,R.color.main_status_bar_blue),1.0f);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressedSupport();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragments = new ArrayList<>();
        if(savedInstanceState == null){
            mPresenter.setNightModeState(false);
            initPager(false, Constants.TYPY_MAIN_PAGER);
        }else {
            //acitivity重建
            mBottomNavigationView.setSelectedItemId(R.id.tab_main_pager);
            initPager(true, Constants.TYPE_SETTING);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initPager(boolean isRecreate, int position) {
        mMainPagerFragment = MainPagerFragment.getInstance(isRecreate, null);
        mFragments.add(mMainPagerFragment);
        initFragment();
        init();
        switchFragment(position);

    }

    private void initFragment() {
        mKnowledgeHierarchyFragment = KnowledgeHierarchyFragment.getInstans(null, null);
        mNavigationFragment = NavigationFragment.getInstance(null, null);
        mGrilsFragment = GrilsFragment.getInstance(null, null);
        CollectFragment collectFragment = CollectFragment.getInstance(null, null);
        SettingFragment settingFragment = SettingFragment.getInstance(null, null);

        mFragments.add(mKnowledgeHierarchyFragment);
        mFragments.add(mNavigationFragment);
        mFragments.add(mGrilsFragment);
        mFragments.add(collectFragment);
        mFragments.add(settingFragment);
    }

    private void init(){
        initNavigationView();
        initBottomNavigationView();
        initDrawerLayout();
    }

    private void initNavigationView() {
        mUserTv = mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_login_tv);
        if(mPresenter.getLoginStatus()){
            showLoginView();
        }else {
            showLoginOutView();
        }
    }

    private void initBottomNavigationView() {
    }

    private void initDrawerLayout() {
    }

    private void switchFragment(int position) {
    }


    @Override
    protected void initEventAndData() {

    }

    @Override
    public void showSwitchNavigation() {

    }

    @Override
    public void showAutoLoginView() {

    }

    @Override
    public void showLoginView() {
        if(mNavigationView == null){
            return;
        }
        mUserTv = mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_login_tv);
        mUserTv.setText(mPresenter.getLoginAccount());
        mUserTv.setOnClickListener(null);
        mNavigationView.getMenu().findItem(R.id.nav_item_logout).setVisible(true);
    }

    @Override
    public void showLoginOutView() {
        mUserTv.setText(R.string.login_in);
        mUserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));

            }
        });
        if(mNavigationView == null){
            return;
        }
        mNavigationView.getMenu().findItem(R.id.nav_item_logout).setVisible(false);
    }

}
