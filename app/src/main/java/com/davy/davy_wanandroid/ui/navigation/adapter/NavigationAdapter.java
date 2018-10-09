package com.davy.davy_wanandroid.ui.navigation.adapter;

import android.app.ActivityOptions;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;
import com.davy.davy_wanandroid.bean.navigation.NavigationListData;
import com.davy.davy_wanandroid.ui.navigation.viewholder.NavigationViewHolder;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.davy.davy_wanandroid.utils.KnowledgeUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * author: Davy
 * date: 2018/10/9
 */
public class NavigationAdapter extends BaseQuickAdapter<NavigationListData, NavigationViewHolder> {

    public NavigationAdapter(int layoutResId, @Nullable List<NavigationListData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(NavigationViewHolder viewHolder, NavigationListData item) {
        if(!TextUtils.isEmpty(item.getName())){
            viewHolder.setText(R.id.item_navigation_tv, item.getName());
            final TagFlowLayout mTagFlowLayout = viewHolder.getView(R.id.item_navigation_flow_layout);
            final List<WanAndroidArticleData> articles = item.getArticles();
            mTagFlowLayout.setAdapter(new TagAdapter<WanAndroidArticleData>(articles) {
                @Override
                public View getView(FlowLayout parent, int position, WanAndroidArticleData wanAndroidArticleData) {
                    TextView mTvName = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.flow_layout_tv, mTagFlowLayout, false);
                    if(wanAndroidArticleData == null){
                        return null;
                    }

                    String title = wanAndroidArticleData.getTitle();
                    mTvName.setPadding(CommonUtils.dp2px(10), CommonUtils.dp2px(10),
                            CommonUtils.dp2px(10), CommonUtils.dp2px(10));
                    mTvName.setText(title);
                    mTvName.setTextColor(CommonUtils.randomColor());

                    mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, FlowLayout parent) {
                            startDetailPager(view, position, parent, articles);
                            return true;
                        }
                    });
                    return mTvName;
                }
            });
        }
    }

    private void startDetailPager(View view, int position, FlowLayout parent, List<WanAndroidArticleData> articles) {
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view,
                view.getWidth() / 2,
                view.getHeight() / 2,
                0,
                0
        );
        KnowledgeUtils.startArticleDetailActivity(parent.getContext(),
                options,
                articles.get(position).getId(),
                articles.get(position).getTitle(),
                articles.get(position).getLink(),
                articles.get(position).isCollect(),
                false,
                false
                );
    }
}
