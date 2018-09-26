package com.davy.davy_wanandroid.presenter;

import android.text.TextUtils;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.base.rx.BaseObsever;
import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.LoginData;
import com.davy.davy_wanandroid.contract.main.RegisterContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.utils.RxUtil;

import javax.inject.Inject;
import io.reactivex.functions.Predicate;

/**
 * author: Davy
 * date: 2018/9/26
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.presenter {

    private DataManager mDataManager;

    @Inject
    public RegisterPresenter(DataManager dataManager){
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void getRegisterData(final String username, final String password, final String repassword) {
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)){
            mView.showSnackBar(WanAndroidApplication.getInstance().getString(R.string.account_password_username_null));
            return;
        }
        if(!password.equals(repassword)){
            mView.showSnackBar(WanAndroidApplication.getInstance().getString(R.string.password_not_same));
            return;
        }
        addSubscribe(mDataManager.getRegisterData(username,password,repassword)
                .compose(RxUtil.<BaseResponse<LoginData>>rxScheduleHelper())
                .compose(RxUtil.<LoginData>handleResult())
                .filter(new Predicate<LoginData>() {
                    @Override
                    public boolean test(LoginData loginData) throws Exception {
                        return !TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(repassword);
                    }
                }).subscribeWith(new BaseObsever<LoginData>(mView,WanAndroidApplication.getInstance().getString(R.string.register_fail)) {

                    @Override
                    public void onNext(LoginData loginData) {
                        mView.showRegisterSuccess();
                    }
                })
        );

    }
}
