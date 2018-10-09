package com.davy.davy_wanandroid.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.activity.BaseActivity;
import com.davy.davy_wanandroid.base.fragment.BaseFragment;
import com.davy.davy_wanandroid.contract.main.MainContract;
import com.davy.davy_wanandroid.core.event.LoginEvent;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.main.MainPresenter;
import com.davy.davy_wanandroid.ui.girls.fragment.GrilsFragment;
import com.davy.davy_wanandroid.ui.knowledgehierarchy.fragment.KnowledgeHierarchyFragment;
import com.davy.davy_wanandroid.ui.main.fragment.CollectFragment;
import com.davy.davy_wanandroid.ui.main.fragment.MainPagerFragment;
import com.davy.davy_wanandroid.ui.main.fragment.SettingFragment;
import com.davy.davy_wanandroid.ui.navigation.fragment.NavigationFragment;
import com.davy.davy_wanandroid.utils.BottomNavigationViewHelper;
import com.davy.davy_wanandroid.utils.CommonAlertDialog;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.davy.davy_wanandroid.utils.RxBus;
import com.davy.davy_wanandroid.utils.StatusBarUtli;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {


    @BindView(R.id.common_toolbar_title_tv)
    TextView mToolbarTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
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
    private int mLastPosition;

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

    @Override
    public void onBackPressedSupport() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1){
            pop();
        }else{
            ActivityCompat.finishAfterTransition(this);
        }
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
        mPresenter.setCurrentPage(Constants.TYPY_MAIN_PAGER);
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
        
        mNavigationView.getMenu().findItem(R.id.nav_item_wan_android).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startMainPager();
                return true;
            }
        });

        mNavigationView.getMenu().findItem(R.id.nav_item_my_collect).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(mPresenter.getLoginStatus()){
                    startCollectFragment();
                    return true;
                }else {
                    CommonUtils.showMessage(MainActivity.this,R.string.login_msg);
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    return true;
                }
            }
        });

        mNavigationView.getMenu().findItem(R.id.nav_item_setting).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startSettingFragment();
                return true;
            }
        });

        mNavigationView.getMenu().findItem(R.id.nav_item_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                logOut();
                return true;
            }
        });


    }

    @OnClick({R.id.main_floating_action_btn})
        void onClick(View view){
            switch (view.getId()){
                case R.id.main_floating_action_btn:
                    jumpToTop();
                    break;
                default:
                    break;
            }

        }

    private void jumpToTop() {
        switch (mPresenter.getCurrentPage()){
            case Constants.TYPY_MAIN_PAGER:
                if(mMainPagerFragment != null){
                    mMainPagerFragment.jumpToTop();
                }
                break;
            case Constants.TYPE_KNOWLEDGE:
                if(mKnowledgeHierarchyFragment != null){
                    mKnowledgeHierarchyFragment.jumpToTop();
                }
                break;
             case Constants.TYPE_NAVIGATION:
                 if(mNavigationFragment != null){
                     mNavigationFragment.jumpToTop();
                 }
                 break;

        }
    }


    private void logOut() {
        CommonAlertDialog.newInstance().showDialog(this, getString(R.string.is_login_out), getString(R.string.ok),
                getString(R.string.no), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        comfimLogOut();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonAlertDialog.newInstance().isCancelDialog(true);
                    }
                });
    }

    private void comfimLogOut() {
        CommonAlertDialog.newInstance().isCancelDialog(true);
        mNavigationView.getMenu().findItem(R.id.nav_item_logout).setVisible(false);
        mPresenter.setLoginStatus(false);
        mPresenter.setLoginAccount("");
        mPresenter.setLoginPassword("");
        RxBus.getDefault().post(new LoginEvent(false));
        startActivity(new Intent(this,LoginActivity.class));
    }

    private void startSettingFragment() {
        mToolbarTitleTv.setText(R.string.setting);
        switchFragment(Constants.TYPE_SETTING);
        mDrawerLayout.closeDrawers();
    }

    private void startCollectFragment() {
        mToolbarTitleTv.setText(R.string.my_collect);
        switchFragment(Constants.TYPE_COLLECT);
        mDrawerLayout.closeDrawers();
    }

    private void startMainPager() {
        mToolbarTitleTv.setText(R.string.home_pager);
        mBottomNavigationView.setVisibility(View.VISIBLE);
        mBottomNavigationView.setSelectedItemId(R.id.tab_main_pager);
        mDrawerLayout.closeDrawers();
    }

    private void initBottomNavigationView() {
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab_main_pager:
                        loadPager(getString(R.string.home_pager), mMainPagerFragment, 0, Constants.TYPY_MAIN_PAGER);
                        break;
                    case R.id.tab_knowledge_hierarchy:
                        loadPager(getString(R.string.knowledge_hierarchy), mKnowledgeHierarchyFragment, 1, Constants.TYPE_KNOWLEDGE);
                        break;
                    case R.id.tab_navigation:
                        loadPager(getString(R.string.navigation), mNavigationFragment, 2, Constants.TYPE_NAVIGATION);
                        break;
                    case R.id.tab_grils:
                        loadPager(getString(R.string.grils_picture), mGrilsFragment, 3, Constants.TYPE_GIRLS);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void loadPager(String title, BaseFragment fragment, int position, int typePage){
        mToolbarTitleTv.setText(title);
        fragment.reload();
        switchFragment(position);
        mPresenter.setCurrentPage(typePage);

    }

    private void initDrawerLayout() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.navigation_open,R.string.navigation_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                View mContent = mDrawerLayout.getChildAt(0);
                float scale = 1 - slideOffset;
                float endScale = 0.8f + scale * 0.2f;
                float startScale = 1 - 0.3f * scale;

                drawerView.setScaleX(startScale);
                drawerView.setScaleY(startScale);
                drawerView.setAlpha(0.6f + 0.4f * (1 - scale));

                mContent.setTranslationX(drawerView.getMeasuredWidth() * (1 - scale));
                mContent.invalidate();
                mContent.setScaleX(endScale);
                mContent.setScaleY(endScale);

            }
        };
        drawerToggle.syncState();
        mDrawerLayout.addDrawerListener(drawerToggle);

    }

    /**
     * 切换fragment
     * @param position
     */
    private void switchFragment(int position) {
        if(position >= Constants.TYPE_COLLECT){
            mFloatingActionBtn.setVisibility(View.INVISIBLE);
            mBottomNavigationView.setVisibility(View.INVISIBLE);
        }else {
            mFloatingActionBtn.setVisibility(View.VISIBLE);
            mBottomNavigationView.setVisibility(View.VISIBLE);
        }
        if(position >= mFragments.size()){
            return;
        }
        BaseFragment targetFragment = mFragments.get(position);
        BaseFragment lastFragment = mFragments.get(mLastPosition);
        mLastPosition = position;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(lastFragment);
        if(!targetFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().remove(targetFragment).commit();
            transaction.add(R.id.fragment_group,targetFragment);
        }
        transaction.show(targetFragment);
        transaction.commitAllowingStateLoss();
    }


    @Override
    protected void initEventAndData() {

    }

    @Override
    public void showSwitchNavigation() {
        if(mBottomNavigationView != null){
            mBottomNavigationView.setSelectedItemId(R.id.tab_navigation);
        }
    }

    @Override
    public void showAutoLoginView() {
        showLoginView();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonAlertDialog.newInstance().isCancelDialog(true);
    }
}
