package com.davy.davy_wanandroid.presenter.main;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.AbstractPresenter;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.base.rx.BaseObsever;
import com.davy.davy_wanandroid.base.rx.BaseSuscriber;
import com.davy.davy_wanandroid.contract.main.MainContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.core.event.AutoLoginEvent;
import com.davy.davy_wanandroid.core.event.LoginEvent;
import com.davy.davy_wanandroid.core.event.NightModeEvent;
import com.davy.davy_wanandroid.core.event.SwitchNavigatinEvent;
import com.davy.davy_wanandroid.utils.RxBus;
import com.davy.davy_wanandroid.utils.RxUtil;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * author: Davy
 * date: 2018/9/29
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public MainPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void attachView(MainContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(NightModeEvent.class)
                .compose(RxUtil.<NightModeEvent>rxflSchedulerHelper())
                .map(new Function<NightModeEvent, Boolean>() {
                    @Override
                    public Boolean apply(NightModeEvent nightModeEvent) throws Exception {
                        return nightModeEvent.isNightMode();
                    }
                })
                .subscribeWith(new BaseSuscriber<Boolean>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_night_mode_cast)) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        mView.useNightMode(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        registerEvent();
                    }
                })
        );

        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .filter(new Predicate<LoginEvent>() {
                    @Override
                    public boolean test(LoginEvent loginEvent) throws Exception {
                        return loginEvent.isLogin();
                    }
                })
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        mView.showLoginView();
                    }
                })
        );

        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .filter(new Predicate<LoginEvent>() {
                    @Override
                    public boolean test(LoginEvent loginEvent) throws Exception {
                        return !loginEvent.isLogin();
                    }
                })
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        mView.showLoginOutView();
                    }
                })
        );

        addSubscribe(RxBus.getDefault().toFlowable(AutoLoginEvent.class)
                .subscribe(new Consumer<AutoLoginEvent>() {
                    @Override
                    public void accept(AutoLoginEvent autoLoginEvent) throws Exception {
                        mView.showAutoLoginView();
                    }
                })
        );

        addSubscribe(RxBus.getDefault().toFlowable(SwitchNavigatinEvent.class)
                .subscribe(new Consumer<SwitchNavigatinEvent>() {
                    @Override
                    public void accept(SwitchNavigatinEvent switchNavigatinEvent) throws Exception {
                        mView.showSwitchNavigation();
                    }
                })
        );
    }

    @Override
    public void setCurrentPage(int page) {
        mDataManager.setCurrentPage(page);
    }

    @Override
    public void setNightModeState(boolean state) {
        mDataManager.setNightModeState(state);
    }
}
