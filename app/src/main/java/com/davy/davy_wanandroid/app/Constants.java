package com.davy.davy_wanandroid.app;

import java.io.File;

/**
 * author: Davy
 * date: 18/9/17
 */
public class Constants {

    public static final long DOUBLE_INTERVAL_TIME = 2000;
    /**
     * Path
     */
    public static final String PATH_DATA = WanAndroidApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/WanAndroidCache";


    /**
     * SharedPreferences
     */
    public static final String SHARED_PREFERENCES = "wanAndroid_sharedPreferences";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String LOGIN_STATUS = "login_status";
    public static final String Current_PAGE = "current_page";
    public static final String NIGHT_MODE_STATE = "night_mode_state";

    /**
     * Tag Fragment
     */
    public static final int TYPY_MAIN_PAGER = 0;
    public static final int TYPE_KNOWLEDGE = 1;
    public static final int TYPE_NAVIGATION = 2;
    public static final int TYPE_GIRLS = 3;
    public static final int TYPE_COLLECT = 4;
    public static final int TYPE_SETTING = 5;

    /**
     * Intent params
     */
    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";

}
