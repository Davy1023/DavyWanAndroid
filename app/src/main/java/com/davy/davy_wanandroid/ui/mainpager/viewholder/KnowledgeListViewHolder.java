package com.davy.davy_wanandroid.ui.mainpager.viewholder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.davy.davy_wanandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Davy
 * date: 18/10/3
 */
public class KnowledgeListViewHolder extends BaseViewHolder {

    @BindView(R.id.item_pager_group)
    CardView mItemPagerGroup;
    @BindView(R.id.item_pager_like_iv)
    ImageView mItemPagerLikeIv;
    @BindView(R.id.item_pager_title)
    TextView mItemPagerTitle;
    @BindView(R.id.item_pager_author)
    TextView mItemPagerAuthor;
    @BindView(R.id.item_pager_tag_green_tv)
    TextView mTagGreenTv;
    @BindView(R.id.item_pager_tag_red_tv)
    TextView mTagRedTv;
    @BindView(R.id.item_pager_chapterName)
    TextView mItemPagerChapterName;
    @BindView(R.id.item_pager_niceDate)
    TextView mItemPagerNiceDate;

    public KnowledgeListViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
