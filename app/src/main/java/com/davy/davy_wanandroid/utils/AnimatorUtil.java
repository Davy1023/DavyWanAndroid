package com.davy.davy_wanandroid.utils;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;

/**
 * author: Davy
 * date: 2018/9/29
 */
public class AnimatorUtil {

    private static LinearOutSlowInInterpolator OUTSLOWINTERPOLATOR = new LinearOutSlowInInterpolator();

    /**
     * 隐藏view
     *
     * @param view
     * @param viewPropertyAnimatorListener
     */
    public static void translateHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener){
        ViewCompat.animate(view)
                .translationY(320)
                .setDuration(400)
                .setInterpolator(OUTSLOWINTERPOLATOR)
                .setListener(viewPropertyAnimatorListener)
                .start();

    }

    /**
     * 显示view
     *
     * @param view
     * @param viewPropertyAnimatorListener
     */
    public static void translateShow(View view,ViewPropertyAnimatorListener viewPropertyAnimatorListener){
        ViewCompat.animate(view)
                .translationY(0)
                .setDuration(400)
                .setInterpolator(OUTSLOWINTERPOLATOR)
                .setListener(viewPropertyAnimatorListener)
                .start();

    }
}
