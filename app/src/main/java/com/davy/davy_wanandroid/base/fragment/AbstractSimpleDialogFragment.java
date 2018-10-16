package com.davy.davy_wanandroid.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.inter.IBase;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: Davy
 * date: 2018/10/15
 */
public abstract class AbstractSimpleDialogFragment extends DialogFragment implements IBase {

    public View mRootView;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);
        initInjector(WanAndroidApplication.getInstance().getApplicationComponent());
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mUnbinder != null && mUnbinder != Unbinder.EMPTY){
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WanAndroidApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取布局id
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initEventAndData();
}
