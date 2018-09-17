package com.davy.davy_wanandroid.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.davy.davy_wanandroid.app.WanAndroidApplication;

/**
 * author: Davy
 * date: 18/9/17
 */
public class CommonUtils {

    public static boolean isNetWorkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) WanAndroidApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
