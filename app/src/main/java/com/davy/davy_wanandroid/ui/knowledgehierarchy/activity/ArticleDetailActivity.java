package com.davy.davy_wanandroid.ui.knowledgehierarchy.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.activity.BaseActivity;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;
import com.davy.davy_wanandroid.contract.KnowledgeHierarchy.ArticleDetailContract;
import com.davy.davy_wanandroid.core.event.CollectEvent;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.KnowledgeHierarchy.ArticleDetailPresenter;
import com.davy.davy_wanandroid.ui.main.activity.LoginActivity;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.davy.davy_wanandroid.utils.RxBus;
import com.davy.davy_wanandroid.utils.StatusBarUtli;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Method;

import butterknife.BindView;

/**
 * author: Davy
 * date: 18/10/6
 */
public class ArticleDetailActivity extends BaseActivity<ArticleDetailPresenter> implements ArticleDetailContract.View {

    @BindView(R.id.article_detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pb_progress)
    ProgressBar mPbProgress;
    @BindView(R.id.webview_detail)
    WebView mWebview;

    private Bundle mBundle;
    private String mTitle;
    private int mId;
    private String mArticleLink;
    private boolean mIsCollect;
    private boolean mIsCommonSite;
    private boolean mIsCollectPage;
    private MenuItem mMenuCollectItem;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_detail;
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
        getBundleData();
        mToolbar.setTitle(Html.fromHtml(mTitle));
        setSupportActionBar(mToolbar);
        StatusBarUtli.immersive(this);
        StatusBarUtli.setPaddingSmart(this, mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsCollect) {
                    RxBus.getDefault().post(new CollectEvent(false));
                } else {
                    RxBus.getDefault().post(new CollectEvent(true));
                }
                onBackPressedSupport();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initEventAndData() {

        mWebview.loadUrl(mArticleLink);

        WebSettings mSettings = mWebview.getSettings();
        if (mPresenter.getAutoCacheState()) {
            mSettings.setAppCacheEnabled(true);
            mSettings.setDomStorageEnabled(true);
            mSettings.setDatabaseEnabled(true);
            if (CommonUtils.isNetWorkConnected()) {
                mSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                mSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
        } else {
            mSettings.setAppCacheEnabled(false);
            mSettings.setDomStorageEnabled(false);
            mSettings.setDatabaseEnabled(false);
            mSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        mSettings.setJavaScriptEnabled(true);
        mSettings.setSupportZoom(true);
        mSettings.setBuiltInZoomControls(true);
        mSettings.setDisplayZoomControls(false);
        mSettings.setUseWideViewPort(true);
        mSettings.setLoadWithOverviewMode(true);
        mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        mWebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(mPbProgress == null){
                    return;
                }
                if( newProgress == 100){
                    mPbProgress.setVisibility(View.GONE);
                }else {
                    mPbProgress.setVisibility(View.VISIBLE);
                    mPbProgress.setProgress(newProgress);
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mBundle = getIntent().getExtras();
        assert mBundle != null;
        mIsCommonSite = (boolean) mBundle.get(Constants.IS_COMMON_SITE);
        if (!mIsCommonSite) {
            unCommonSiteEvent(menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_article_common, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_share:
                mPresenter.shareEventPermissionVerify(new RxPermissions(this));
                break;
            case R.id.item_collect:
                collectEvent();
                break;
            case R.id.item_system_browser:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mArticleLink)));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (Constants.MENU_BUILDER.equalsIgnoreCase(menu.getClass().getSimpleName())) {
                try {
                    @SuppressLint("PrivateApi")
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void showCollectArticleData(WanAndroidArticleListData wanAndroidArticleListData) {
        mIsCollect = true;
        mMenuCollectItem.setTitle(R.string.cancel_collect);
        mMenuCollectItem.setIcon(R.mipmap.ic_toolbar_like_p);
        CommonUtils.showSnackMessage(this, getString(R.string.collet_success));
    }

    @Override
    public void showCancelCollectAtrticleData(WanAndroidArticleListData wanAndroidArticleListData) {
        mIsCollect = false;
        if (!mIsCollectPage) {
            mMenuCollectItem.setTitle(R.string.my_collect);
        }
        mMenuCollectItem.setIcon(R.mipmap.ic_toolbar_like_n);
        CommonUtils.showSnackMessage(this, getString(R.string.cancel_collet_success));
    }

    @Override
    public void shareEvent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_type_url, getString(R.string.app_name), mTitle, mArticleLink));
        intent.setType("text/plain");
        startActivity(intent);
    }

    @Override
    public void shareError() {
        CommonUtils.showSnackMessage(this, getString(R.string.write_permission_not_allowed));
    }

    private void collectEvent() {
        if (!mPresenter.getLoginStatus()) {
            CommonUtils.showSnackMessage(this, getString(R.string.login_tint));
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            if (mMenuCollectItem.getTitle().equals(R.string.my_collect)) {
                mPresenter.addCollectArtitle(mId);
            } else {
                if (mIsCollectPage) {
                    mPresenter.cancelCollectPageArticle(mId);
                } else {
                    mPresenter.cancelCollectArticle(mId);
                }
            }
        }
    }


    private void unCommonSiteEvent(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_acticle, menu);
        mMenuCollectItem = menu.findItem(R.id.item_collect);
        if (mIsCollect) {
            mMenuCollectItem.setTitle(R.string.cancel_collect);
            mMenuCollectItem.setIcon(R.mipmap.ic_toolbar_like_p);
        } else {
            mMenuCollectItem.setTitle(R.string.my_collect);
            mMenuCollectItem.setIcon(R.mipmap.ic_toolbar_like_n);
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            supportFinishAfterTransition();
        }
    }

    private void getBundleData() {
        mBundle = getIntent().getExtras();
        assert mBundle != null;
        mTitle = (String) mBundle.get(Constants.ARTICLE_TITLE);
        mId = (int) mBundle.get(Constants.ARTICLE_ID);
        mArticleLink = (String) mBundle.get(Constants.ARTICLE_LINK);
        mIsCollect = (boolean) mBundle.get(Constants.IS_COLLECT);
        mIsCollectPage = (boolean) mBundle.get(Constants.IS_COLLECT_PAGE);
        mIsCommonSite = (boolean) mBundle.get(Constants.IS_COMMON_SITE);
    }
}
