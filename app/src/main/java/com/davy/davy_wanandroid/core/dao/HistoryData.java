package com.davy.davy_wanandroid.core.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: Davy
 * date: 18/10/18
 */

@Entity
public class HistoryData {

    @Id(autoincrement = true)
    private Long id;

    private long date;

    private String data;

    @Generated(hash = 1371145256)
    public HistoryData(Long id, long date, String data) {
        this.id = id;
        this.date = date;
        this.data = data;
    }

    @Generated(hash = 422767273)
    public HistoryData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
