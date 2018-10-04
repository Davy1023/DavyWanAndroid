package com.davy.davy_wanandroid.ui.knowledgehierarchy.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.bean.knowledgehierarchy.KnowledgeHierarchyData;
import com.davy.davy_wanandroid.ui.knowledgehierarchy.viewholder.KnowledgeHierarchyViewHolder;

import java.util.List;

/**
 * author: Davy
 * date: 18/10/4
 */
public class KnowledgeHierarchyAdapter extends BaseQuickAdapter<KnowledgeHierarchyData, KnowledgeHierarchyViewHolder> {

    public KnowledgeHierarchyAdapter(int layoutResId, @Nullable List<KnowledgeHierarchyData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(KnowledgeHierarchyViewHolder viewHolder, KnowledgeHierarchyData itemData) {
        if(itemData.getName() == null){
            return;
        }
        viewHolder.setText(R.id.item_knowledge_hierarchy_title, itemData.getName());
        if(itemData.getChildren() == null){
            return;
        }
        StringBuffer content = new StringBuffer();
        for (KnowledgeHierarchyData data : itemData.getChildren()){
            content.append(data.getName()).append("  ");
        }

        viewHolder.setText(R.id.item_knowledge_hierarchy_content, content.toString());

    }
}
