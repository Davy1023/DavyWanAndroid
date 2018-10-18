package com.davy.davy_wanandroid.core.db;

import com.davy.davy_wanandroid.core.dao.HistoryData;

import java.util.List;

/**
 * author: Davy
 * date: 18/10/18
 */
public interface DbHelper {

    /**
     * 添加历史数据
     * @param data
     * @return List<HistoryData>
     */
    List<HistoryData> addHistoryData(String data);

    /**
     * 清理历史数据
     */
    void clearnHistoryData();

    /**
     * 获取所有历史数据
     * @return List<HistoryData>
     */
    List<HistoryData> getAllHistoryData();
}
