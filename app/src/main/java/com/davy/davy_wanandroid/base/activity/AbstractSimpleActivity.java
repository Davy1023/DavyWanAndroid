package com.davy.davy_wanandroid.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.inter.IBase;
import com.davy.davy_wanandroid.utils.ActivityCollector;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * author: Davy
 * date: 18/9/19
 */
public abstract class AbstractSimpleActivity extends SupportActivity implements IBase{

    private Unbinder mUnbinder;
    private AbstractSimpleActivity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initInjector(WanAndroidApplication.getInstance().getApplicationComponent());
        mActivity = this;
        mUnbinder = ButterKnife.bind(this);
        ActivityCollector.getInstance().addActivity(this);
        onViewCreated();
        initToolbar();
        initEventAndData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.getInstance().removeActivity(this);
        if(mUnbinder != null && mUnbinder != Unbinder.EMPTY){
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }

    /**
     * 当前activity的UI布局
     * @return 布局id
     */
    protected abstract int getLayoutId() ;

    /**
     * 在初始化数据前执行
     */
    protected abstract void onViewCreated();

    /**
     * 初始化Toolbar
     */
    protected abstract void initToolbar();

    /**
     * 初始化数据
     */
    protected abstract void initEventAndData();



}
