package com.davy.davy_wanandroid.presenter.navigation;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.base.rx.BaseObsever;
import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.navigation.NavigationListData;
import com.davy.davy_wanandroid.contract.navigation.NavigationContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.utils.RxUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * author: Davy
 * date: 18/10/8
 */
public class NavigationPresenter extends BasePresenter<NavigationContract.View> implements NavigationContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public NavigationPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void attachView(NavigationContract.View view) {
        super.attachView(view);
    }

    @Override
    public void getNavigationListData(boolean isShowError) {
        addSubscribe(mDataManager.getNavigationListData()
                .compose(RxUtil.<BaseResponse<List<NavigationListData>>>rxScheduleHelper())
                .compose(RxUtil.<List<NavigationListData>>handleResult())
                .subscribeWith(new BaseObsever<List<NavigationListData>>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_navigation_data), isShowError) {
                    @Override
                    public void onNext(List<NavigationListData> navigationListData) {
                        mView.showNavigationListaData(navigationListData);
                    }
                })
        );
    }
}
