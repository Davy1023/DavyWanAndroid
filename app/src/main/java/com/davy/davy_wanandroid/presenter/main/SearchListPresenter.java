package com.davy.davy_wanandroid.presenter.main;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.base.rx.BaseObsever;
import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;
import com.davy.davy_wanandroid.contract.main.SearchListContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.core.event.CollectEvent;
import com.davy.davy_wanandroid.utils.RxBus;
import com.davy.davy_wanandroid.utils.RxUtil;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * author: Davy
 * date: 18/10/21
 */
public class SearchListPresenter extends BasePresenter<SearchListContract.View> implements SearchListContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public SearchListPresenter(DataManager dataManager) {
        super(dataManager);
        mDataManager = dataManager;
    }

    @Override
    public void attachView(SearchListContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .filter(new Predicate<CollectEvent>() {
                    @Override
                    public boolean test(CollectEvent collectEvent) throws Exception {
                        return !collectEvent.isCancelCollectSucces();
                    }
                })
                .subscribe(new Consumer<CollectEvent>() {
                    @Override
                    public void accept(CollectEvent collectEvent) throws Exception {
                        mView.showCollectSucces();
                    }
                })
        );

        addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .filter(new Predicate<CollectEvent>() {
                    @Override
                    public boolean test(CollectEvent collectEvent) throws Exception {
                        return collectEvent.isCancelCollectSucces();
                    }
                })
                .subscribe(new Consumer<CollectEvent>() {
                    @Override
                    public void accept(CollectEvent collectEvent) throws Exception {
                        mView.showCancleCollectSucces();
                    }
                })
        );
    }


    @Override
    public void getSearchList(int page, String k, boolean isShowError) {
        addSubscribe(mDataManager.getSearchList(page, k)
                .compose(RxUtil.<BaseResponse<WanAndroidArticleListData>>rxScheduleHelper())
                .compose(RxUtil.<WanAndroidArticleListData>handleResult())
                .subscribeWith(new BaseObsever<WanAndroidArticleListData>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_list_data), isShowError) {
                    @Override
                    public void onNext(WanAndroidArticleListData wanAndroidArticleListData) {
                        mView.showSearchList(wanAndroidArticleListData);
                    }
                })
        );
    }

    @Override
    public void addCollectArticle(final int position, final WanAndroidArticleData wanAndroidArticleData) {
        addSubscribe(mDataManager.addCollectArticle(wanAndroidArticleData.getId())
                .compose(RxUtil.<BaseResponse<WanAndroidArticleListData>>rxScheduleHelper())
                .compose(RxUtil.<WanAndroidArticleListData>hanleCollteResult())
                .subscribeWith(new BaseObsever<WanAndroidArticleListData>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_collect)) {
                    @Override
                    public void onNext(WanAndroidArticleListData wanAndroidArticleListData) {
                        wanAndroidArticleData.setCollect(true);
                        mView.showCollectArticleData(position, wanAndroidArticleData, wanAndroidArticleListData);
                    }
                })
        );
    }

    @Override
    public void cancelCollectArticle(final int position, final WanAndroidArticleData wanAndroidArticleData) {
        addSubscribe(mDataManager.cancelCollectArticle(wanAndroidArticleData.getId())
                .compose(RxUtil.<BaseResponse<WanAndroidArticleListData>>rxScheduleHelper())
                .compose(RxUtil.<WanAndroidArticleListData>hanleCollteResult())
                .subscribeWith(new BaseObsever<WanAndroidArticleListData>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_cancle_collect)) {
                    @Override
                    public void onNext(WanAndroidArticleListData wanAndroidArticleListData) {
                        wanAndroidArticleData.setCollect(false);
                        mView.showCancelArticleData(position, wanAndroidArticleData, wanAndroidArticleListData);
                    }
                })
        );
    }

    public boolean getNightModeState(){
        return mDataManager.getNightModeState();
    }
}
