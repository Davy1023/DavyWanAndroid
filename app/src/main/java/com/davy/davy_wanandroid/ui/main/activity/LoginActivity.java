package com.davy.davy_wanandroid.ui.main.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.base.activity.BaseActivity;
import com.davy.davy_wanandroid.contract.main.LoginContract;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.main.LoginPresenter;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.davy.davy_wanandroid.utils.LogHelper;
import com.davy.davy_wanandroid.utils.StatusBarUtli;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * author: Davy
 * date: 2018/9/27
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.login_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.login_account_edit)
    EditText mAccountEdit;
    @BindView(R.id.login_password_edit)
    EditText mPasswordEdit;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.login_register_btn)
    Button mRegisterBtn;

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initToolbar() {
        StatusBarUtli.immersive(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedSupport();
            }
        });
    }

    @OnClick({R.id.login_register_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.login_register_btn:
                startRegisterPager();
                break;
            default:
                break;
        }
    }

    private void startRegisterPager() {
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(mRegisterBtn, mRegisterBtn.getWidth() / 2,
                mRegisterBtn.getHeight() / 2, 0, 0);
        startActivity(new Intent(this,RegisterActivity.class),options.toBundle());
    }

    @Override
    protected void initEventAndData() {
            subscribeLoginClickEvent();
    }

    private void subscribeLoginClickEvent() {
        mPresenter.addRxBindingSubscribe(RxView.clicks(mLoginBtn)
        .throttleFirst(1, TimeUnit.SECONDS)
        .filter(new Predicate<Object>() {
            @Override
            public boolean test(Object o) {
                return mPresenter != null;
            }
        }).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        mPresenter.getLoginData(mAccountEdit.getText().toString().trim(),
                                mPasswordEdit.getText().toString().trim());
                    }
                }));

    }

    @Override
    public void showLoginSuccess() {
        CommonUtils.showMessage(this,getString(R.string.login_success));
        onBackPressedSupport();
    }

}
