package com.davy.davy_wanandroid.ui.main.fragment;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.base.fragment.BaseDialogFragment;
import com.davy.davy_wanandroid.bean.main.UsefulSiteData;
import com.davy.davy_wanandroid.contract.main.SiteDialogContract;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.main.SiteDialogPresenter;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.davy.davy_wanandroid.utils.KnowledgeUtils;
import com.davy.davy_wanandroid.utils.LogHelper;
import com.davy.davy_wanandroid.widget.CircularRevealAnim;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author: Davy
 * date: 2018/10/15
 */
public class SiteDialogFragment extends BaseDialogFragment<SiteDialogPresenter> implements SiteDialogContract.View,
        CircularRevealAnim.AnimListener,
        ViewTreeObserver.OnPreDrawListener {

    @BindView(R.id.common_toolbar_title_tv)
    TextView mToolTitle;
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.useful_sites_flow_layout)
    TagFlowLayout mSitesFlowLayout;
    @BindView(R.id.usage_scroll_view)
    NestedScrollView mScrollView;

    private List<UsefulSiteData> mUsefulSiteListData;
    private CircularRevealAnim mRevealAnim;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_site_dialog;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);
    }

    @Override
    protected void initEventAndData() {
        initCircleAnimation();
        initToolbar();
        mUsefulSiteListData = new ArrayList<>();
        mPresenter.getUsefulSiteData();
    }

    private void initCircleAnimation() {
        mRevealAnim = new CircularRevealAnim();
        mRevealAnim.setAnimListener(this);
        mToolTitle.getViewTreeObserver().addOnPreDrawListener(this);
    }

    private void initToolbar(){
        mToolTitle.setText(getString(R.string.useful_sites));
        if(mPresenter.getNightModeState()){
            createToolBarView(R.color.comment_text, R.color.colorCard, R.drawable.ic_arrow_back_white_24dp);
        }else{
            createToolBarView(R.color.title_black, R.color.white, R.drawable.ic_arrow_back_grey_24dp);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRevealAnim.hide(mToolTitle, mRootView);
            }
        });
    }

    private void createToolBarView(@ColorRes int textColor, @ColorRes int backgroundColor, @DrawableRes int navigationIcon) {
        mToolTitle.setTextColor(ContextCompat.getColor(getContext(), textColor));
        mToolbar.setBackgroundColor(ContextCompat.getColor(getActivity(), backgroundColor));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getContext(), navigationIcon));
    }

    @Override
    public void showUsefulSiteData(final List<UsefulSiteData> siteDataList) {
        mUsefulSiteListData = siteDataList;
        mSitesFlowLayout.setAdapter(new TagAdapter<UsefulSiteData>(mUsefulSiteListData) {
            @Override
            public View getView(FlowLayout parent, int position, UsefulSiteData usefulSiteData) {
                assert getActivity() != null;
                TextView mTv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.flow_layout_tv, parent, false);
                assert usefulSiteData != null;
                String siteDataName = usefulSiteData.getName();
                mTv.setText(siteDataName);
                setSiteItemBackgroundColor(mTv);
                mSitesFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        startSitePager(view, position);
                        return true;
                    }
                });
                return mTv;
            }
        });
    }

    private void startSitePager(View view, int position) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, getString(R.string.share_view));
        KnowledgeUtils.startArticleDetailActivity(getActivity(),
                options,
                mUsefulSiteListData.get(position).getId(),
                mUsefulSiteListData.get(position).getName().trim(),
                mUsefulSiteListData.get(position).getLink().trim(),
                false,
                false,
                false);
    }

    private void setSiteItemBackgroundColor(TextView tv) {
        tv.setBackgroundColor(CommonUtils.randomTagColor());
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
    }

    private void initDialog() {
        Window window = getDialog().getWindow();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = (int) (displayMetrics.widthPixels * 0.98);
        assert window != null;
        window.setLayout(width, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.TOP);
        window.setWindowAnimations(R.style.DialogEmptyAnimation);


    }

    @Override
    public boolean onPreDraw() {
        mToolTitle.getViewTreeObserver().removeOnPreDrawListener(this);
        mRevealAnim.show(mToolTitle, mRootView);
        return true;
    }

    @Override
    public void onHideAnimationEnd() {
        dismissAllowingStateLoss();
    }

    @Override
    public void onShowAnimationEnd() {

    }
}
