package com.davy.davy_wanandroid.base.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;

/**
 * author: Davy
 * date: 18/9/29
 */
public abstract class BaseRootFragment<T extends BasePresenter> extends BaseFragment<T> {

    private static final int NORMAL_STATE = 0;
    private static final int LOADING_STATE = 1;
    private static final int ERROR_STATE = 2;

    private int currentState = NORMAL_STATE;
    private ViewGroup mNormalView;
    private View mLoadingView;
    private View mErrorView;
    private LottieAnimationView mLoadingAnimation;

    @Override
    protected void initEventAndData() {
        if (getView() == null) {
            return;
        }
        mNormalView = getView().findViewById(R.id.normal_view);
        if (mNormalView == null) {
            throw new IllegalStateException("The subclass of RootActivity must contain a View named 'mNormalView");
        }
        if (!(mNormalView.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException("mNormalView's ParentView should be a ViewGroup");
        }

        ViewGroup parent = (ViewGroup) mNormalView.getParent();
        View.inflate(_mActivity, R.layout.loading_view, parent);
        View.inflate(_mActivity, R.layout.error_view, parent);
        mLoadingView = parent.findViewById(R.id.loading_group);
        mErrorView = parent.findViewById(R.id.error_group);
        TextView reloadText = mErrorView.findViewById(R.id.error_reload_tv);
        reloadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reload();
            }
        });
        mLoadingAnimation = mLoadingView.findViewById(R.id.loading_animation);
        mErrorView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mNormalView.setVisibility(View.VISIBLE);

    }


    @Override
    public void showLoading() {
        if (currentState == LOADING_STATE) {
            return;
        }
        hideCurentView();
        currentState = LOADING_STATE;
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingAnimation.setAnimation("loading_bus.json");
        mLoadingAnimation.loop(true);
        mLoadingAnimation.playAnimation();
    }

    @Override
    public void showError() {
        if(currentState == ERROR_STATE){
            return;
        }
        hideCurentView();
        currentState = ERROR_STATE;
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNormal() {
        if(currentState == NORMAL_STATE){
            return;
        }
        hideCurentView();
        currentState = NORMAL_STATE;
        mNormalView.setVisibility(View.VISIBLE);
    }

    private void hideCurentView() {
        switch (currentState){
            case NORMAL_STATE:
               if(mNormalView == null){
                return;
            }
                mNormalView.setVisibility(View.INVISIBLE);
                break;
            case LOADING_STATE:
                mLoadingAnimation.cancelAnimation();
                mLoadingView.setVisibility(View.GONE);
                break;
            case ERROR_STATE:
                mErrorView.setVisibility(View.GONE);
                break;
             default:
                 break;
        }
    }

    @Override
    public void onDestroyView() {
        if (mLoadingAnimation != null) {
            mLoadingAnimation.cancelAnimation();
        }
        super.onDestroyView();
    }
}
