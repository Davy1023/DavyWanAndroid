package com.davy.davy_wanandroid.utils;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.ui.knowledgehierarchy.activity.ArticleDetailActivity;
import com.davy.davy_wanandroid.ui.knowledgehierarchy.activity.KnowledgeHierarchyDetailActivity;
import com.davy.davy_wanandroid.ui.main.activity.SearchListActivity;

/**
 * author: Davy
 * date: 18/10/7
 */
public class KnowledgeUtils {

    public static void startArticleDetailActivity(Context context, ActivityOptions activityOptions, int id, String articleTitle,
                                                  String articleLink, boolean isCollect,
                                                  boolean isCollectPage, boolean isCommonSite){

        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(Constants.ARTICLE_ID, id);
        intent.putExtra(Constants.ARTICLE_LINK, articleLink);
        intent.putExtra(Constants.ARTICLE_TITLE, articleTitle);
        intent.putExtra(Constants.IS_COLLECT, isCollect);
        intent.putExtra(Constants.IS_COLLECT_PAGE, isCollectPage);
        intent.putExtra(Constants.IS_COMMON_SITE, isCommonSite);
        if(activityOptions != null && !Build.MANUFACTURER.contains("samsung") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            context.startActivity(intent, activityOptions.toBundle());
        }else {
            context.startActivity(intent);
        }
    }

    public static void startKnowledgeHierarchyDetailActivity(Context context, boolean isSingleChapter,
                                                             String superChapterName, String chapterName, int chapterId){
        Intent intent = new Intent(context, KnowledgeHierarchyDetailActivity.class);
        intent.putExtra(Constants.IS_SINGLE_CHAPTER, isSingleChapter);
        intent.putExtra(Constants.SUPER_CHAPTER_NAME, superChapterName);
        intent.putExtra(Constants.CHAPTER_NAME, chapterName);
        intent.putExtra(Constants.CHAPTER_ID, chapterId);
        context.startActivity(intent);
    }

    public static void startSearchListActivity(Context context, String text){
        Intent intent = new Intent(context, SearchListActivity.class);
        intent.putExtra(Constants.SEARCH_TEXT, text);
        context.startActivity(intent);
    }

}
