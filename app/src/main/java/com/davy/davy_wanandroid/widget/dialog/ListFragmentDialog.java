package com.davy.davy_wanandroid.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.contract.girls.GirlsContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: Davy
 * date: 18/10/13
 */
public class ListFragmentDialog extends DialogFragment {

    @BindView(R.id.recyclerViewList)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_cancle)
    Button mBtnCancle;
    @BindView(R.id.rl_root)
    RelativeLayout mRlRoot;

    private View mView;
    private static Context mContext;
    private FragmentManager mManager;
    private ArrayList<String> mDatas = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private ListDialogAdapter mListDialogAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setWindowAnimations(R.style.animate_list_dialog);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        mView = inflater.inflate(R.layout.dialog_list, null);
        ButterKnife.bind(this, mView);

        initRecyClerView();

        initAdapter();

        return mView;
    }

    public static ListFragmentDialog getInstance(Context context) {
        mContext = context;
        ListFragmentDialog fragment = new ListFragmentDialog();
        return fragment;
    }


    public void showDialog(FragmentManager manager, ArrayList<String> datas, OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        this.mManager = manager;
        this.mDatas = datas;
        show(this.mManager, "");
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;
        window.setAttributes(windowParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @OnClick({R.id.rl_root, R.id.btn_cancle})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_root:
                dismiss();
                break;
            case R.id.btn_cancle:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private void initRecyClerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    private void initAdapter() {
        if(mListDialogAdapter == null){
            mListDialogAdapter = new ListDialogAdapter(mContext, mDatas);
            mRecyclerView.setAdapter(mListDialogAdapter);
            mListDialogAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(view, position);
                    }
                    dismiss();
                }
            });
        }else {
            mListDialogAdapter.updateDatas(mDatas);
        }
    }

    class ListDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private ArrayList<String> mDatas;
        private LayoutInflater mInflater;
        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public ListDialogAdapter(Context context, ArrayList<String> datas) {
            this.mContext = context;
            this.mDatas = datas;
            mInflater = LayoutInflater.from(this.mContext);

        }

        public void updateDatas(ArrayList<String> datas) {
            this.mDatas = datas;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.list_dialog_item, parent, false);
            return new DialogViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if(holder instanceof DialogViewHolder){
                DialogViewHolder dialogViewHolder = (DialogViewHolder) holder;
                dialogViewHolder.mBtnItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mOnItemClickListener != null){
                            mOnItemClickListener.onItemClick(view, position);
                        }
                    }
                });
                dialogViewHolder.mBtnItem.setText(mDatas.get(position));
            }
        }

        @Override
        public int getItemCount() {
            if(mDatas != null && mDatas.size() > 0){
                return mDatas.size();
            }else{
                return 0;
            }
        }

        class DialogViewHolder extends RecyclerView.ViewHolder {

            public Button mBtnItem;

            public DialogViewHolder(View itemView) {
                super(itemView);
                mBtnItem = itemView.findViewById(R.id.btn_item);
            }
        }
    }

}
