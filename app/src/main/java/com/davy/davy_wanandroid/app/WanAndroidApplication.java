package com.davy.davy_wanandroid.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.davy.davy_wanandroid.BuildConfig;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerApplicationComponent;
import com.davy.davy_wanandroid.di.module.ApplicationModule;
import com.davy.davy_wanandroid.di.module.HttpModule;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * author: Davy
 * date: 18/9/15
 */
public class WanAndroidApplication extends Application {

    private static WanAndroidApplication instance;
    private RefWatcher mRefWatcher;
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpModule(new HttpModule())
                .build();


        instance = this;

        initGreenDao();

        initLogger();


        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }

        mRefWatcher = LeakCanary.install(this);
    }

    //全局设置刷新头部和尾部,默认使用日间模式
    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //设置全局主题颜色
                layout.setPrimaryColorsId(R.color.colorPrimary,android.R.color.white);
                return new DeliveryHeader(context);
            }
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new BallPulseFooter(context).setAnimatingColor(ContextCompat.getColor(context,R.color.colorPrimary));
            }
        });
    }

    public static synchronized WanAndroidApplication getInstance(){
        return instance;
    }

    public static RefWatcher getRefWatcher(Context context){
        WanAndroidApplication application = (WanAndroidApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

    private void initGreenDao() {
    }

    private void initLogger() {
        //debug版本下控制台打印log
        if(BuildConfig.DEBUG){
            Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder()
                        .tag(getString(R.string.app_name)).build()));
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(level == TRIM_MEMORY_UI_HIDDEN){
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    public ApplicationComponent getApplicationComponent(){
        if(mApplicationComponent == null){
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .httpModule(new HttpModule())
                    .build();
        }

        return mApplicationComponent;

    }
}
