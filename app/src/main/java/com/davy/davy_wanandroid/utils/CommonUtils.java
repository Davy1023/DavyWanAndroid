package com.davy.davy_wanandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.ui.main.activity.MainActivity;

import java.util.logging.Logger;

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

    public static void showSnackMessage(final Activity activity, String msg){
        LogHelper.e("showSnackMessage:" + msg);
        //去掉虚拟按键
        activity.getWindow().getDecorView().setSystemUiVisibility(
                //隐藏虚拟按键栏 | 防止点击屏幕时,隐藏虚拟按键栏又弹了出来
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
        final Snackbar snackbar = Snackbar.make(activity.getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(ContextCompat.getColor(activity, R.color.white));
        snackbar.setAction("ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                //隐藏SnackBar时记得恢复隐藏虚拟按键栏,不然屏幕底部会多出一块空白布局出来,和难看
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }).show();
        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });
    }

    /**
     * 消息提醒
     * @param activity
     * @param msg
     */
    public static void showMessage(Activity activity, String msg){
        LogHelper.e("showMessage:" + msg);
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
    }

    public static void showMessage(Activity activity, int msg){
        LogHelper.e("showMessage:" + msg);
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 泛型转换工具
     * @param object
     * @param <T> 转换得到的泛型对象
     * @return T
     */
    public static <T> T cast(Object object){
        return (T) object;
    }
}
