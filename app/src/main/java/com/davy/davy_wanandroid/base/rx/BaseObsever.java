package com.davy.davy_wanandroid.base.rx;

import android.text.TextUtils;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.view.AbstractView;
import com.davy.davy_wanandroid.core.http.exception.ServerException;
import com.davy.davy_wanandroid.utils.LogHelper;
import io.reactivex.observers.ResourceObserver;
import retrofit2.HttpException;

/**
 * author: Davy
 * date: 2018/9/26
 */
public abstract class BaseObsever<T> extends ResourceObserver<T> {
    private AbstractView mView;
    private String mErrorMsg;
    private boolean isShowError = true;

    public BaseObsever(AbstractView view){
        this.mView = view;
    }

    public BaseObsever(AbstractView view,String msg){
        this.mView = view;
        this.mErrorMsg = msg;
    }

    public BaseObsever(AbstractView view,boolean isShow){
        this.mView = view;
        this.isShowError = isShow;
    }

    public BaseObsever(AbstractView view, String msg, boolean isShow){
        this.mView = view;
        this.mErrorMsg = msg;
        this.isShowError = isShow;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if(mView == null){
            return;
        }
        if(mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)){
            mView.showErrorMsg(mErrorMsg);
        }else if(e instanceof ServerException){
            mView.showErrorMsg(e.toString());
        }else if(e instanceof HttpException){
            mView.showErrorMsg(WanAndroidApplication.getInstance().getString(R.string.http_error));
        }else{
            mView.showErrorMsg(WanAndroidApplication.getInstance().getString(R.string.unknow_error));
            LogHelper.d(e.toString());
        }
        if(isShowError){
            mView.showError();
        }
    }
}
