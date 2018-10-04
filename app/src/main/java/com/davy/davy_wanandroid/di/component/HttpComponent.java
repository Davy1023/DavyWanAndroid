package com.davy.davy_wanandroid.di.component;

import com.davy.davy_wanandroid.di.scope.CustomScopeName;
import com.davy.davy_wanandroid.ui.main.activity.LoginActivity;
import com.davy.davy_wanandroid.ui.main.activity.MainActivity;
import com.davy.davy_wanandroid.ui.main.activity.RegisterActivity;
import com.davy.davy_wanandroid.ui.main.fragment.MainPagerFragment;

import dagger.Component;

/**
 * author: Davy
 * date: 18/9/17
 */
@CustomScopeName
@Component(dependencies = ApplicationComponent.class)
public interface HttpComponent {

    //首页
    void inject (RegisterActivity registerActivity);
    void inject (LoginActivity loginActivity);
    void inject (MainActivity mainActivity);
    void inject (MainPagerFragment mainPagerFragment);
}
