package com.davy.davy_wanandroid.app;

import com.davy.davy_wanandroid.R;

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
    public static final String CURRENT_PAGE = "current_page";
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

    /**
     * MainPager data
     */

    public static final String LOGIN_DATA = "login_data";
    public static final String Banner_DATA = "banner_data";
    public static final String ARTICLE_LIST_DATA = "article_list_data";

    /**
     * Refresh theme color
     */
    public static final int BLUE_COLOR = R.color.colorPrimary;

    /**
     * Phone MANUFACTURER
     */
    public static final String SAMSUNG = "samsung";

    /**
     * share key
     */
    public static final String AUTO_CACHE_STATE = "auto_cache_state";
    public static final String NO_IMAGE_STATE = "no_image_state";


    public static final String ARTICLE_LINK = "article_link";

    public static final String ARTICLE_TITLE = "article_title";

    public static final String ARTICLE_ID = "article_id";

    public static final String IS_COLLECT = "is_collect";

    public static final String IS_COMMON_SITE = "is_common_site";

    public static final String IS_COLLECT_PAGE = "is_collect_page";

    public static final String CHAPTER_ID = "chapter_id";

    public static final String IS_SINGLE_CHAPTER = "is_single_chapter";

    public static final String CHAPTER_NAME = "is_chapter_name";

    public static final String SUPER_CHAPTER_NAME = "super_chapter_name";


    public static final String MENU_BUILDER = "MenuBuilder";

}
