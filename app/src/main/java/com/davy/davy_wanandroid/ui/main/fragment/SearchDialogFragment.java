package com.davy.davy_wanandroid.ui.main.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.base.fragment.BaseDialogFragment;
import com.davy.davy_wanandroid.bean.main.TopSearchData;
import com.davy.davy_wanandroid.contract.main.SearchContract;
import com.davy.davy_wanandroid.core.dao.HistoryData;
import com.davy.davy_wanandroid.di.component.ApplicationComponent;
import com.davy.davy_wanandroid.di.component.DaggerHttpComponent;
import com.davy.davy_wanandroid.presenter.main.SearchPresenter;
import com.davy.davy_wanandroid.ui.main.adapter.SearchHistoryAdapter;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.davy.davy_wanandroid.utils.KeyBoardUtils;
import com.davy.davy_wanandroid.utils.KnowledgeUtils;
import com.davy.davy_wanandroid.widget.CircularRevealAnim;
import com.jakewharton.rxbinding2.view.RxView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * author: Davy
 * date: 18/10/20
 */
public class SearchDialogFragment extends BaseDialogFragment<SearchPresenter> implements SearchContract.View,
        CircularRevealAnim.AnimListener,
        ViewTreeObserver.OnPreDrawListener {

    @BindView(R.id.search_tv)
    TextView mSearchTv;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.search_tint_tv)
    TextView mSearchHintTv;
    @BindView(R.id.top_search_flow_layout)
    TagFlowLayout mTagFlowLayout;
    @BindView(R.id.search_history_clear_all_tv)
    TextView mClearAll;
    @BindView(R.id.search_history_rv)
    RecyclerView mRecyclerView;

    private CircularRevealAnim mCircularRevealAnim;
    private List<TopSearchData> mTopSearchDataList;
    private SearchHistoryAdapter mSearchHistoryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);
    }

    @Override
    protected void initEventAndData() {
        initCircleAnimation();
        initRecyclerView();

        mTopSearchDataList = new ArrayList<>();
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(mSearchEdit.getText().toString())){
                    mSearchHintTv.setText(getString(R.string.search_hint));
                }else {
                    mSearchHintTv.setText("");
                }
            }
        });
        mPresenter.addRxBindingSubscribe(RxView.clicks(mSearchTv)
                .throttleFirst(1, TimeUnit.SECONDS)
                .filter(new Predicate<Object>() {

                    @Override
                    public boolean test(Object o) throws Exception {
                        return !TextUtils.isEmpty(mSearchEdit.getText().toString().trim());
                    }
                }).subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        mPresenter.addHistoryData(mSearchEdit.getText().toString().trim());
                        setHistoryTvStatus(false);
                    }
                })
        );
        showHistoryData(mPresenter.getAllHistoryData());
        mPresenter.getTopSearchData();
    }

    @OnClick({R.id.search_back_ib, R.id.search_history_clear_all_tv})
    void onClick(View view){
        switch (view.getId()){
            case R.id.search_back_ib:
                backEvent();
                break;
            case R.id.search_history_clear_all_tv:
                clearHistoryData();
                break;
            default:
                break;
        }
    }

    private void backEvent() {
        KeyBoardUtils.closeKeyBoard(getActivity(), mSearchEdit);
        mCircularRevealAnim.hide(mSearchEdit, mRootView);
    }

    private void clearHistoryData() {
        mPresenter.cleanHistoryData();
        mSearchHistoryAdapter.replaceData(mPresenter.getAllHistoryData());
        setHistoryTvStatus(true);
    }

    private void setHistoryTvStatus(boolean isClearAll) {
        mClearAll.setEnabled(!isClearAll);
        if(isClearAll){
            setHistoryTvStatus(R.color.search_grey_gone, R.drawable.ic_clear_all_gone);
        }else {
            setHistoryTvStatus(R.color.search_grey, R.drawable.ic_clear_all);
        }
    }

    private void setHistoryTvStatus( int textColor, int ic_clearDrawable) {
        Drawable drawable;
        mClearAll.setTextColor(ContextCompat.getColor(getActivity(), textColor));
        drawable = ContextCompat.getDrawable(getActivity(), ic_clearDrawable);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mClearAll.setCompoundDrawables(drawable, null, null, null);
        mClearAll.setCompoundDrawablePadding(CommonUtils.dp2px(6));
    }


    private void initRecyclerView() {
        mSearchHistoryAdapter = new SearchHistoryAdapter(R.layout.item_search_history, null);
        mSearchHistoryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                searchHistoryData(adapter, position);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mSearchHistoryAdapter);
    }

    private void searchHistoryData(BaseQuickAdapter adapter, int position) {
        HistoryData historyData = (HistoryData) adapter.getData().get(position);
        mPresenter.addHistoryData(historyData.getData());
        mSearchEdit.setText(historyData.getData());
        mSearchEdit.setSelection(mSearchEdit.getText().length());
        setHistoryTvStatus(false);
    }

    private void initCircleAnimation() {
        mCircularRevealAnim = new CircularRevealAnim();
        mCircularRevealAnim.setAnimListener(this);
        mSearchEdit.getViewTreeObserver().addOnPreDrawListener(this);
    }


    private void initDialog() {
        Window window = getDialog().getWindow();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = (int) (displayMetrics.widthPixels * 0.98);
        assert window != null;
        window.setLayout(width, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.TOP);
        window.setWindowAnimations(R.style.DialogEmptyAnimation);


    }

    @Override
    public void showHistoryData(List<HistoryData> historyDataList) {
        if(historyDataList == null && historyDataList.size() <= 0){
            setHistoryTvStatus(true);
            return;
        }
        setHistoryTvStatus(false);
        Collections.reverse(historyDataList);
        mSearchHistoryAdapter.replaceData(historyDataList);
    }

    @Override
    public void showTopSearchData(List<TopSearchData> topSearchDataList) {
        mTopSearchDataList = topSearchDataList;
        mTagFlowLayout.setAdapter(new TagAdapter<TopSearchData>(mTopSearchDataList) {

            @Override
            public View getView(FlowLayout parent, int position, TopSearchData topSearchData) {
                assert getActivity() != null;
                TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.flow_layout_tv, parent, false);
                assert topSearchData != null;
                String name = topSearchData.getName();
                tv.setText(name);
                setItemBackground(tv);
                mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        showTopSearchView(position);
                        return true;
                    }
                });
                return tv;
            }
        });
    }

    private void showTopSearchView(int position) {
        mPresenter.addHistoryData(mTopSearchDataList.get(position).getName().trim());
        setHistoryTvStatus(false);
        mSearchEdit.setText(mTopSearchDataList.get(position).getName().trim());
        mSearchEdit.setSelection(mSearchEdit.getText().length());
    }

    private void setItemBackground(TextView tv) {
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        tv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
    }

    @Override
    public void jumpToSearchListActivity() {
        KnowledgeUtils.startSearchListActivity(getActivity(), mSearchEdit.getText().toString().trim());
    }

    @Override
    public boolean onPreDraw() {
        mSearchEdit.getViewTreeObserver().removeOnPreDrawListener(this);
        mCircularRevealAnim.show(mSearchEdit, mRootView);
        return true;
    }

    @Override
    public void onHideAnimationEnd() {
        mSearchEdit.setText("");
        dismissAllowingStateLoss();
    }

    @Override
    public void onShowAnimationEnd() {
        KeyBoardUtils.openKeyBoard(getActivity(), mSearchEdit);
    }

}
