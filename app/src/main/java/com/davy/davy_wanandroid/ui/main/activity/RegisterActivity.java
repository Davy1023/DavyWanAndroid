package com.davy.davy_wanandroid.ui.main.activity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.base.activity.BaseActivity;
import com.davy.davy_wanandroid.contract.main.RegisterContract;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.main.RegisterPresenter;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.davy.davy_wanandroid.utils.StatusBarUtli;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * author: Davy
 * date: 18/9/26
 */
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {

    @BindView(R.id.common_toolbar_title_tv)
    TextView mTitleTv;
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.register_password_edit)
    EditText mPasswordEdit;
    @BindView(R.id.register_account_edit)
    EditText mAccountEdit;
    @BindView(R.id.register_confirm_password_edit)
    EditText mConfirmPasswordEdit;
    @BindView(R.id.register_btn)
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
        return R.layout.activity_register;
    }

    @Override
    protected void initToolbar() {
        StatusBarUtli.immersive(this);
        StatusBarUtli.setPaddingSmart(this,mToolbar);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.register_bac));
        mTitleTv.setText(R.string.register);
        mTitleTv.setTextColor(ContextCompat.getColor(this,R.color.white));
        mTitleTv.setTextSize(20);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressedSupport();
            }
        });
    }

    @Override
    protected void initEventAndData() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null){
            mAccountEdit.requestFocus();
            inputMethodManager.showSoftInput(mAccountEdit,0);
        }
        mPresenter.addRxBindingSubscribe(RxView.clicks(mRegisterBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(Object o) {
                        return mPresenter != null;
                    }
                }).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        register();
                    }
                })
        );
    }

    @Override
    public void showRegisterSuccess() {
        CommonUtils.showSnackMessage(this,getString(R.string.register_success));
        onBackPressedSupport();
    }

    private void register() {
        mPresenter.getRegisterData(mAccountEdit.getText().toString().trim(),
                mPasswordEdit.getText().toString().trim(),
                mConfirmPasswordEdit.getText().toString().trim());
    }
}
