package com.davy.davy_wanandroid.contract.girls;

import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.bean.girls.GirlsImageData;

import java.util.List;

/**
 * author: Davy
 * date: 18/10/11
 */
public interface GirlsContract {

    interface View extends AbstractView{

        /**
         * show girsImageData
         * @param girlsImageDataList
         */
        void showGirlsListData(List<GirlsImageData> girlsImageDataList);
    }

    interface Presenter extends AbstractPresenter<View>{

        /**
         * get GirlsListData
         * @param isShowError
         */
        void getGirlsListData(String type, int count, int pageIndex, boolean isShowError);
    }
}
