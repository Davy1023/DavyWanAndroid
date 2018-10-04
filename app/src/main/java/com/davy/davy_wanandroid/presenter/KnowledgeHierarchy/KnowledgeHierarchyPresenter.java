package com.davy.davy_wanandroid.presenter.KnowledgeHierarchy;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.base.rx.BaseObsever;
import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.knowledgehierarchy.KnowledgeHierarchyData;
import com.davy.davy_wanandroid.contract.KnowledgeHierarchy.KnowledgeHierarchyContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.utils.RxUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * author: Davy
 * date: 18/10/4
 */
public class KnowledgeHierarchyPresenter extends BasePresenter<KnowledgeHierarchyContract.View> implements KnowledgeHierarchyContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public KnowledgeHierarchyPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void getKnowledgeHierarchyData(boolean isShowError) {
        addSubscribe(mDataManager.getKnowledgeHierarchyData()
                .compose(RxUtil.<BaseResponse<List<KnowledgeHierarchyData>>>rxScheduleHelper())
                .compose(RxUtil.<List<KnowledgeHierarchyData>>handleResult())
                .subscribeWith(new BaseObsever<List<KnowledgeHierarchyData>>(mView, WanAndroidApplication.getInstance().getString(R.string.fial_knowledgehierarchy_data), isShowError) {
                    @Override
                    public void onNext(List<KnowledgeHierarchyData> knowledgeHierarchyDataList) {
                        mView.showKnowledgeHierarchyData(knowledgeHierarchyDataList);
                    }
                })
        );
    }
}
