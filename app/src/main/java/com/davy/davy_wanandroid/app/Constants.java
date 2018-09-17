package com.davy.davy_wanandroid.app;

import java.io.File;

/**
 * author: Davy
 * date: 18/9/17
 */
public class Constants {

    /**
     * Path
     */
    public static final String PATH_DATA = WanAndroidApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/WanAndroidCache";
}
