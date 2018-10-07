package com.davy.davy_wanandroid.presenter.KnowledgeHierarchy;

import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.contract.KnowledgeHierarchy.KnowledgeHierarchyDetailContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.core.event.SwitchNavigatinEvent;
import com.davy.davy_wanandroid.utils.RxBus;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * author: Davy
 * date: 18/10/5
 */
public class KnowledgeHierarchyDetailPresenter extends BasePresenter<KnowledgeHierarchyDetailContract.View> implements KnowledgeHierarchyDetailContract.Presenter{

    @Inject
    public KnowledgeHierarchyDetailPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void attachView(KnowledgeHierarchyDetailContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(SwitchNavigatinEvent.class)
                .subscribe(new Consumer<SwitchNavigatinEvent>() {
                    @Override
                    public void accept(SwitchNavigatinEvent switchNavigatinEvent) throws Exception {
                        mView.showSwithNavitation();
                    }
                })
        );
    }
}
