package com.davy.davy_wanandroid.presenter.main;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.base.rx.BaseObsever;
import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.TopSearchData;
import com.davy.davy_wanandroid.contract.main.SearchContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.core.dao.HistoryData;
import com.davy.davy_wanandroid.utils.RxUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * author: Davy
 * date: 18/10/18
 */
public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter{

    private DataManager mDataManager;

    @Inject
    public SearchPresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    @Override
    public void attachView(SearchContract.View view) {
        super.attachView(view);
    }

    @Override
    public List<HistoryData> getAllHistoryData() {
        return mDataManager.getAllHistoryData();
    }

    @Override
    public void addHistoryData(final String data) {
        addSubscribe(Observable.create(new ObservableOnSubscribe<List<HistoryData>>() {
            @Override
            public void subscribe(ObservableEmitter<List<HistoryData>> e) throws Exception {
                List<HistoryData> historyDataList = mDataManager.addHistoryData(data);
                e.onNext(historyDataList);
            }
        })
                .compose(RxUtil.<List<HistoryData>>rxScheduleHelper())
                .subscribe(new Consumer<List<HistoryData>>() {
                    @Override
                    public void accept(List<HistoryData> historyDataList) throws Exception {
                            mView.jumpToSearchListActivity();
                    }
                }));
    }

    @Override
    public void getTopSearchData() {
        addSubscribe(mDataManager.getTopSearchData()
                .compose(RxUtil.<BaseResponse<List<TopSearchData>>>rxScheduleHelper())
                .compose(RxUtil.<List<TopSearchData>>handleResult())
                .subscribeWith(new BaseObsever<List<TopSearchData>>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_hot_data)) {
                    @Override
                    public void onNext(List<TopSearchData> topSearchDataList) {
                        mView.showTopSearchData(topSearchDataList);
                    }
                })
        );
    }

    @Override
    public void cleanHistoryData() {
        mDataManager.clearnHistoryData();
    }
}
