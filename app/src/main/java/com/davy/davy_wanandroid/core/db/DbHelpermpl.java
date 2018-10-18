package com.davy.davy_wanandroid.core.db;

import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.core.dao.DaoSession;
import com.davy.davy_wanandroid.core.dao.HistoryData;
import com.davy.davy_wanandroid.core.dao.HistoryDataDao;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * author: Davy
 * date: 18/10/18
 */
public class DbHelpermpl implements DbHelper {

    private static final int HISTORY_LIST_SIZE = 10;

    private final DaoSession mDaoSession;
    private String mData;
    private HistoryDataDao mHistoryDataDao;
    private List<HistoryData> mHistoryDataList;
    private HistoryData mHistoryData;

    @Inject
    DbHelpermpl(){

        mDaoSession = WanAndroidApplication.getInstance().getDaoSession();
    }
    @Override
    public List<HistoryData> addHistoryData(String data) {
        this.mData = data;
        getHistoryDataList();
        createHistoryData();

        if(historyDataForward()){
            return mHistoryDataList;
        }

        if(mHistoryDataList.size() < HISTORY_LIST_SIZE){
            getHistoryDataDao().insert(mHistoryData);
        }else{
            mHistoryDataList.remove(0);
            mHistoryDataList.add(mHistoryData);
            getHistoryDataDao().deleteAll();
            getHistoryDataDao().insertInTx(mHistoryDataList);
        }


        return mHistoryDataList;
    }

    /**
     * 历史数据迁移
     * @return 返回true表示查询数据已存在，只需将其前移到第一项历史记录，否则添加新的历史记录
     */
    private boolean historyDataForward(){
        Iterator<HistoryData> iterator = mHistoryDataList.iterator();
        while (iterator.hasNext()){
            HistoryData historyData = iterator.next();
            if(historyData.getData().equals(mData)){
                mHistoryDataList.remove(historyData);
                mHistoryDataList.add(mHistoryData);
                getHistoryDataDao().deleteAll();
                getHistoryDataDao().insertInTx(mHistoryDataList);
                return true;
            }
        }
        return false;
    }

    private void createHistoryData() {
        mHistoryData = new HistoryData();
        mHistoryData.setDate(System.currentTimeMillis());
        mHistoryData.setData(mData);
    }

    private void getHistoryDataList() {
        mHistoryDataList = getHistoryDataDao().loadAll();
    }

    private HistoryDataDao getHistoryDataDao() {
        mHistoryDataDao = mDaoSession.getHistoryDataDao();
        return mHistoryDataDao;
    }

    @Override
    public void clearnHistoryData() {
        mDaoSession.getHistoryDataDao().deleteAll();
    }

    @Override
    public List<HistoryData> getAllHistoryData() {
        return mDaoSession.getHistoryDataDao().loadAll();
    }
}
