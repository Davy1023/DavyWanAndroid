package com.davy.davy_wanandroid.contract.KnowledgeHierarchy;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.bean.knowledgehierarchy.KnowledgeHierarchyData;

import java.util.List;

/**
 * author: Davy
 * date: 18/10/4
 */
public interface KnowledgeHierarchyContract {

    interface View extends AbstractView{

        /**
         * 显示知识体系列表数据
         * @param knowledgeHierarchyDataList
         */
        void showKnowledgeHierarchyData(List<KnowledgeHierarchyData> knowledgeHierarchyDataList);
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * 获取知识体系列表数据
         * @param isShowError
         */
        void getKnowledgeHierarchyData(boolean isShowError);
    }
}
