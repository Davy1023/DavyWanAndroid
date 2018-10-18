package com.davy.davy_wanandroid.di.module;

import android.content.Context;

import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.core.db.DbHelper;
import com.davy.davy_wanandroid.core.http.HttpHelper;
import com.davy.davy_wanandroid.core.http.HttpHelperImpl;
import com.davy.davy_wanandroid.core.prefs.PreferencesHelper;
import com.davy.davy_wanandroid.core.prefs.PreferencesHelperImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * author: Davy
 * date: 18/9/17
 */
@Module
public class ApplicationModule {

    private Context mContext;

    public ApplicationModule(Context context){
        this.mContext = context;
    }

    @Singleton
    @Provides
    WanAndroidApplication provideApplication(){
        return (WanAndroidApplication) mContext.getApplicationContext();
    }

    @Singleton
    @Provides
    Context provideContext(){
        return mContext;
    }

    @Singleton
    @Provides
    HttpHelper provideHttpHelper (HttpHelperImpl httpHelperImpl){
        return httpHelperImpl;
    }

    @Singleton
    @Provides
    PreferencesHelper providePreferencesHelper (PreferencesHelperImpl preferencesHelper){
        return preferencesHelper;
    }

    @Singleton
    @Provides
    DataManager provideDataManager(HttpHelper httpHelper, PreferencesHelper preferencesHelper, DbHelper dbHelper){
        return new DataManager(httpHelper, preferencesHelper, dbHelper);
    }
}
