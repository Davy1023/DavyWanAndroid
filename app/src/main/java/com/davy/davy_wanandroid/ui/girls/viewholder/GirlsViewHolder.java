package com.davy.davy_wanandroid.ui.girls.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.davy.davy_wanandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Davy
 * date: 2018/10/12
 */
public class GirlsViewHolder extends BaseViewHolder {

    @BindView(R.id.rl_root)
    RelativeLayout mLayout;
    @BindView(R.id.iv_girls)
    ImageView mIvGirls;

    public GirlsViewHolder(View view) {
        super(view);
        ButterKnife.bind(view);
    }
}
