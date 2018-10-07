package com.davy.davy_wanandroid.contract.KnowledgeHierarchy;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;

/**
 * author: Davy
 * date: 18/10/5
 */
public interface KnowledgeHierarchyDetailContract {

    interface View  extends AbstractView{

        void showSwithNavitation();
    }

    interface Presenter extends AbstractPresenter<View>{

    }
}
