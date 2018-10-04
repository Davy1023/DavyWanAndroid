package com.davy.davy_wanandroid.ui.knowledgehierarchy.viewholder;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.davy.davy_wanandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Davy
 * date: 18/10/4
 */
public class KnowledgeHierarchyViewHolder extends BaseViewHolder {

    @BindView(R.id.item_knowledge_hierarchy_title)
    TextView mTitle;
    @BindView(R.id.item_knowledge_hierarchy_content)
    TextView mContent;

    public KnowledgeHierarchyViewHolder(View view) {
        super(view);
        ButterKnife.bind(view);
    }
}
