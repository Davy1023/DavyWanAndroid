package com.davy.davy_wanandroid.ui.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.base.fragment.BaseFragment;
import com.davy.davy_wanandroid.contract.main.SettingContract;
import com.davy.davy_wanandroid.core.event.NightModeEvent;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.main.SettingPresenter;
import com.davy.davy_wanandroid.utils.CacheUtils;
import com.davy.davy_wanandroid.utils.RxBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Cache;

/**
 * author: Davy
 * date: 18/9/29
 */
public class SettingFragment extends BaseFragment<SettingPresenter> implements SettingContract.View, CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.cb_setting_cache)
    AppCompatCheckBox mSettingCache;
    @BindView(R.id.cb_setting_night)
    AppCompatCheckBox mSettingNight;
    @BindView(R.id.tv_setting_clear)
    TextView mTvClear;
    @BindView(R.id.ll_setting_clear)
    LinearLayout mSettingClear;

    private File mCacheFile;

    public static SettingFragment getInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
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
        mCacheFile = new File(Constants.PATH_CACHE);
        mTvClear.setText(CacheUtils.getCacheSize(mCacheFile));
        mSettingCache.setChecked(mPresenter.getAutoCacheState());
        mSettingNight.setChecked(mPresenter.getNightModeState());
        mSettingCache.setOnCheckedChangeListener(this);
        mSettingNight.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.ll_setting_clear})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_setting_clear:
                clearCache();
                break;
            default:
                break;
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cb_setting_cache:
                mPresenter.setAutoCacheState(isChecked);
                break;
            case R.id.cb_setting_night:
                mPresenter.setNightModeState(isChecked);
                RxBus.getDefault().post(new NightModeEvent(isChecked));
                break;
             default:
                 break;
        }
    }

    private void clearCache(){
        CacheUtils.deleteDir(mCacheFile);
        mTvClear.setText(CacheUtils.getCacheSize(mCacheFile));
    }
}
