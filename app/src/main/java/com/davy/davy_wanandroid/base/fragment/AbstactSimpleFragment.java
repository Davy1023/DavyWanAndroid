package com.davy.davy_wanandroid.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.inter.IBase;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * author: Davy
 * date: 18/9/19
 */
public abstract class AbstactSimpleFragment extends SupportFragment implements IBase{

    private Unbinder mUnbinder;
    private long clickTime;
    public boolean isInnerFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(),container,false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInjector(WanAndroidApplication.getInstance().getApplicationComponent());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mUnbinder!=null && mUnbinder!=Unbinder.EMPTY){
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WanAndroidApplication.getRefWatcher(_mActivity);
        refWatcher.watch(this);

    }

    /**
     * 懒加载 当Fragment可见时，加载网络数据
     * @param savedInstanceState
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initEventAndData();
    }

    /**
     * 处理back回退事件
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if(getChildFragmentManager().getBackStackEntryCount() > 1){
            popChild();
        }else {
            if(isInnerFragment){
                _mActivity.finish();
                return true;
            }

            long currentTime = System.currentTimeMillis();
            if(currentTime - clickTime > Constants.DOUBLE_INTERVAL_TIME){
                CommonUtils.showSnackMessage(_mActivity,getString(R.string.double_click_exit_message));
                clickTime = System.currentTimeMillis();
            }else {
                _mActivity.finish();
            }
        }
        return true;
    }

    /**
     * 当前fragment的UI布局
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 在onCreateview中做初始化工作
     */
    protected void initView(){

    }

    /**
     * 初始化数据
     */
    protected abstract void initEventAndData();


}
