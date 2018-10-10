package com.davy.davy_wanandroid.presenter.KnowledgeHierarchy;

import android.Manifest;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.base.rx.BaseObsever;
import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;
import com.davy.davy_wanandroid.contract.KnowledgeHierarchy.ArticleDetailContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.utils.RxUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * author: Davy
 * date: 18/10/6
 */
public class ArticleDetailPresenter extends BasePresenter<ArticleDetailContract.View> implements ArticleDetailContract.Presenter{

    private DataManager mDataManager;

    @Inject
    public ArticleDetailPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public boolean getAutoCacheState() {
        return mDataManager.getAutoCacheState();
    }

    @Override
    public void addCollectArtitle(int id) {
        addSubscribe(mDataManager.addCollectArticle(id)
                .compose(RxUtil.<BaseResponse<WanAndroidArticleListData>>rxScheduleHelper())
                .compose(RxUtil.<WanAndroidArticleListData>hanleCollteResult())
                .subscribeWith(new BaseObsever<WanAndroidArticleListData>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_collect)) {
                    @Override
                    public void onNext(WanAndroidArticleListData wanAndroidArticleListData) {
                        mView.showCollectArticleData(wanAndroidArticleListData);
                    }
                })
        );
    }

    @Override
    public void cancelCollectArticle(int id) {
        addSubscribe(mDataManager.cancelCollectArticle(id)
                .compose(RxUtil.<BaseResponse<WanAndroidArticleListData>>rxScheduleHelper())
                .compose(RxUtil.<WanAndroidArticleListData>hanleCollteResult())
                .subscribeWith(new BaseObsever<WanAndroidArticleListData>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_cancle_collect)) {
                    @Override
                    public void onNext(WanAndroidArticleListData wanAndroidArticleListData) {
                        mView.showCancelCollectAtrticleData(wanAndroidArticleListData);
                    }
                })
        );
    }

    @Override
    public void cancelCollectPageArticle(int id) {
        addSubscribe(mDataManager.cancelCollectPageArticle(id)
                .compose(RxUtil.<BaseResponse<WanAndroidArticleListData>>rxScheduleHelper())
                .compose(RxUtil.<WanAndroidArticleListData>hanleCollteResult())
                .subscribeWith(new BaseObsever<WanAndroidArticleListData>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_cancle_collect)) {
                    @Override
                    public void onNext(WanAndroidArticleListData wanAndroidArticleListData) {
                        mView.showCancelCollectAtrticleData(wanAndroidArticleListData);
                    }
                })
        );
    }

    @Override
    public void shareEventPermissionVerify(RxPermissions rxPermissions) {
        addSubscribe(rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if(granted){
                            mView.shareEvent();
                        }else {
                            mView.shareError();
                        }
                    }
                })
        );
    }
}
