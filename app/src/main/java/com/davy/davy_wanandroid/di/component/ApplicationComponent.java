package com.davy.davy_wanandroid.di.component;

import com.davy.davy_wanandroid.core.http.api.WanAndroidApi;
import com.davy.davy_wanandroid.di.module.ApplicationModule;
import com.davy.davy_wanandroid.di.module.HttpModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * author: Davy
 * date: 18/9/17
 */
@Singleton
@Component(modules = {ApplicationModule.class, HttpModule.class})
public interface ApplicationComponent {

    WanAndroidApi getWanAndroidApi();
}
