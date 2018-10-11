package com.davy.davy_wanandroid.presenter.girls;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.base.rx.BaseObsever;
import com.davy.davy_wanandroid.bean.BaseGankResponse;
import com.davy.davy_wanandroid.bean.girls.GirlsImageData;
import com.davy.davy_wanandroid.contract.girls.GirlsContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.utils.RxUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * author: Davy
 * date: 18/10/11
 */
public class GirlsPresenter extends BasePresenter<GirlsContract.View> implements GirlsContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public GirlsPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void getGirlsListData(String type, int count, int pageIndex, boolean isShowError) {
        addSubscribe(mDataManager.getGirlsListData(type, count, pageIndex)
                .compose(RxUtil.<BaseGankResponse<List<GirlsImageData>>>rxScheduleHelper())
                .compose(RxUtil.<List<GirlsImageData>>handleGirlsResult())
                .subscribeWith(new BaseObsever<List<GirlsImageData>>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_grils_data), isShowError) {
                    @Override
                    public void onNext(List<GirlsImageData> girlsImageDataList) {
                        mView.showGirlsListData(girlsImageDataList);
                    }
                })


        );
    }
}
