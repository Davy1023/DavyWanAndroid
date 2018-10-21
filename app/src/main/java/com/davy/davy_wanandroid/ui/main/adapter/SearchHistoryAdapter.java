package com.davy.davy_wanandroid.ui.main.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.core.dao.HistoryData;
import com.davy.davy_wanandroid.ui.main.viewholder.SearchHistoryViewHolder;

import java.util.List;

/**
 * author: Davy
 * date: 18/10/20
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<HistoryData, SearchHistoryViewHolder> {

    public SearchHistoryAdapter(int layoutResId, @Nullable List<HistoryData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(SearchHistoryViewHolder viewHolder, HistoryData item) {
        viewHolder.setText(R.id.item_search_history_tv, item.getData());
        viewHolder.setTextColor(R.id.item_search_history_tv, mContext.getResources().getColor(R.color.comment_text));
        viewHolder.addOnClickListener(R.id.item_search_history_tv);
    }
}
