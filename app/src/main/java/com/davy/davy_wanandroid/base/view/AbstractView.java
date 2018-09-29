package com.davy.davy_wanandroid.base.view;

/**
 * View的基类
 *
 * author: Davy
 * date: 18/9/15
 */
public interface AbstractView {

    void showLoading();

    void showNormal();

    void showErrorMsg(String msg);

    void showError();

    void reload();

    void showLoginView();

    void showLoginOutView();

    void useNightMode(boolean isNightMode);

    void showSnackBar(String msg);
}
