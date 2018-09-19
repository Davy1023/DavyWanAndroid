package com.davy.davy_wanandroid.utils;

import android.app.Activity;

import java.util.HashSet;
import java.util.Set;

/**
 * author: Davy
 * date: 18/9/19
 */
public class ActivityCollector {

    private static ActivityCollector sActivityCollector;

    public synchronized static ActivityCollector getInstance(){
        if(sActivityCollector == null){
            sActivityCollector = new ActivityCollector();
        }

        return sActivityCollector;
    }

    private Set<Activity> mActivities;

    public void addActivity(Activity activity){
        if(mActivities == null){
            mActivities = new HashSet<>();
        }
        mActivities.add(activity);
    }

    public void removeActivity(Activity activity){
        if(mActivities!=null){
            mActivities.remove(activity);
        }
    }
}
