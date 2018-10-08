package com.davy.davy_wanandroid.presenter.main;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.base.rx.BaseObsever;
import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;
import com.davy.davy_wanandroid.contract.main.CollectContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.core.event.CollectEvent;
import com.davy.davy_wanandroid.utils.RxBus;
import com.davy.davy_wanandroid.utils.RxUtil;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * author: Davy
 * date: 2018/10/8
 */
public class CollectPresenter extends BasePresenter<CollectContract.View> implements CollectContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public CollectPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void attachView(CollectContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .subscribe(new Consumer<CollectEvent>() {
                    @Override
                    public void accept(CollectEvent collectEvent) throws Exception {
                        mView.showRefreshEvent();
                    }
                })
        );
    }

    @Override
    public void getCollectArticleList(int page, boolean isShowError) {
        addSubscribe(mDataManager.getCollectArticleList(page)
                .compose(RxUtil.<BaseResponse<WanAndroidArticleListData>>rxScheduleHelper())
                .compose(RxUtil.<WanAndroidArticleListData>handleResult())
                .subscribeWith(new BaseObsever<WanAndroidArticleListData>(mView, WanAndroidApplication.getInstance().getString(R.string.failed_to_obtain_collection_data, isShowError)) {
                    @Override
                    public void onNext(WanAndroidArticleListData wanAndroidArticleListData) {
                        mView.showCollectArticleList(wanAndroidArticleListData);
                    }
                })
        );
    }

    @Override
    public void cancelCollectPageArticle(final int position, final WanAndroidArticleData wanAndroidArticleData) {
        addSubscribe(mDataManager.cancelCollectPageArticle(wanAndroidArticleData.getId())
                .compose(RxUtil.<BaseResponse<WanAndroidArticleListData>>rxScheduleHelper())
                .compose(RxUtil.<WanAndroidArticleListData>hanleCollteResult())
                .subscribeWith(new BaseObsever<WanAndroidArticleListData>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_cancle_collect)) {
                    @Override
                    public void onNext(WanAndroidArticleListData wanAndroidArticleListData) {
                        wanAndroidArticleData.setCollect(false);
                        mView.showCancelCollectPageArticle(position, wanAndroidArticleData, wanAndroidArticleListData);
                    }
                })
        );
    }
}
