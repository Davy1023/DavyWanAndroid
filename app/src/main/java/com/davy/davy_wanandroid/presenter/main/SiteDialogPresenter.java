package com.davy.davy_wanandroid.presenter.main;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.base.rx.BaseObsever;
import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.UsefulSiteData;
import com.davy.davy_wanandroid.contract.main.SiteDialogContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.utils.LogHelper;
import com.davy.davy_wanandroid.utils.RxUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * author: Davy
 * date: 2018/10/15
 */
public class SiteDialogPresenter extends BasePresenter<SiteDialogContract.View> implements SiteDialogContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public SiteDialogPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void getUsefulSiteData() {
        addSubscribe(mDataManager.getUsefulSiteData()
                .compose(RxUtil.<BaseResponse<List<UsefulSiteData>>>rxScheduleHelper())
                .compose(RxUtil.<List<UsefulSiteData>>handleResult())
                .subscribeWith(new BaseObsever<List<UsefulSiteData>>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_site_data)) {
                    @Override
                    public void onNext(List<UsefulSiteData> siteDataList) {
                        mView.showUsefulSiteData(siteDataList);
                        LogHelper.e("siteDataList==" + siteDataList);
                    }
                }));

    }
}
