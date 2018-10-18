package com.davy.davy_wanandroid.contract.main;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.bean.main.TopSearchData;
import com.davy.davy_wanandroid.core.dao.HistoryData;

import java.util.List;

/**
 * author: Davy
 * date: 18/10/18
 */
public interface SearchContract {

    interface View extends AbstractView{

        /**
         * show historyData
         * @param historyDataList
         */
        void showHistoryData(List<HistoryData> historyDataList);

        /**
         * show topSearchData
         * @param topSearchDataList
         */
        void showTopSearchData(List<TopSearchData> topSearchDataList);
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 获取所有历史数据
         * @return List<HistoryData>
         */
        List<HistoryData> getAllHistoryData();

        /**
         * 添加历史数据
         * @param data
         */
        void addHistoryData(String data);

        /**
         * 获取热搜数据
         */
        void getTopSearchData();

        /**
         * 清理历史数据
         */
        void cleanHistoryData();
    }
}
