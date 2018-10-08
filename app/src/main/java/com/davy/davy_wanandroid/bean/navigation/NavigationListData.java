package com.davy.davy_wanandroid.bean.navigation;

import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;

import java.io.Serializable;
import java.util.List;

/**
 * author: Davy
 * date: 18/10/8
 */
public class NavigationListData implements Serializable {

    private List<WanAndroidArticleData> articles;
    private int cid;
    private String name;

    public List<WanAndroidArticleData> getArticles() {
        return articles;
    }

    public void setArticles(List<WanAndroidArticleData> articles) {
        this.articles = articles;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
