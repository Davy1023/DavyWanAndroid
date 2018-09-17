package com.davy.davy_wanandroid.di.component;

import com.davy.davy_wanandroid.di.scope.CustomScopeName;
import dagger.Component;

/**
 * author: Davy
 * date: 18/9/17
 */
@CustomScopeName
@Component(dependencies = ApplicationComponent.class)
public interface HttpComponent {

}
