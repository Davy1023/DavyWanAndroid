package com.davy.davy_wanandroid.ui.mainpager.adapter;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;
import com.davy.davy_wanandroid.ui.mainpager.viewholder.KnowledgeListViewHolder;

import java.util.List;

/**
 * author: Davy
 * date: 18/10/3
 */
public class ArticleListAdapter extends BaseQuickAdapter<WanAndroidArticleData, KnowledgeListViewHolder> {

    private boolean isColletPage;
    private boolean isSerchPage;
    private boolean isNightMode;
    public ArticleListAdapter(int layoutResId, @Nullable List<WanAndroidArticleData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(KnowledgeListViewHolder viewHolder, WanAndroidArticleData article) {
        if(!TextUtils.isEmpty(article.getTitle())){
            viewHolder.setTag(R.id.item_pager_title, Html.fromHtml(article.getTitle()));
        }
        if(article.isCollect() || isColletPage){
            viewHolder.setImageResource(R.id.item_pager_like_iv,R.drawable.icon_like);
        }else{
            viewHolder.setImageResource(R.id.item_pager_like_iv, R.drawable.icon_like_article_not_selected);
        }
        if(!TextUtils.isEmpty(article.getAuthor())){
            viewHolder.setText(R.id.item_pager_author, article.getAuthor());
        }

        setTag(viewHolder, article);

        if(!TextUtils.isEmpty(article.getChapterName())){
            String classifyName = article.getSuperChapterName() + " / " + article.getChapterName();
            if(isColletPage){
                viewHolder.setText(R.id.item_pager_chapterName, article.getChapterName());
            }else {
                viewHolder.setText(R.id.item_pager_chapterName, classifyName);
            }
        }
        if(!TextUtils.isEmpty(article.getNiceDate())){
            viewHolder.setText(R.id.item_pager_niceDate, article.getNiceDate());
        }
        if(isSerchPage){
            CardView cardView = viewHolder.getView(R.id.item_pager_group);
            cardView.setForeground(null);
            if(isNightMode){
                cardView.setBackground(ContextCompat.getDrawable(mContext, R.color.card_color));
            }else {
                cardView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selector_search_item_bac));
            }
        }

        viewHolder.addOnClickListener(R.id.item_pager_chapterName);
        viewHolder.addOnClickListener(R.id.item_pager_like_iv);
        viewHolder.addOnClickListener(R.id.item_pager_tag_red_tv);
    }

    private void setTag(KnowledgeListViewHolder viewHolder, WanAndroidArticleData article) {
        viewHolder.getView(R.id.item_pager_tag_green_tv).setVisibility(View.GONE);
        viewHolder.getView(R.id.item_pager_tag_red_tv).setVisibility(View.GONE);
        if(isColletPage){
            return;
        }

        if(article.getSuperChapterName().contains(mContext.getString(R.string.navigation))){
            setRedTag(viewHolder, R.string.navigation);
        }

        if(article.getNiceDate().contains(mContext.getString(R.string.minute))
                || article.getNiceDate().contains(mContext.getString(R.string.hour))
                || article.getNiceDate().contains(mContext.getString(R.string.one_day))){
            viewHolder.getView(R.id.item_pager_tag_green_tv).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.item_pager_tag_green_tv, R.string.text_new);
            viewHolder.setTextColor(R.id.item_pager_tag_green_tv, ContextCompat.getColor(mContext, R.color.light_green));
            viewHolder.setBackgroundRes(R.id.item_pager_tag_green_tv, R.drawable.shape_tag_green_background);
        }
    }

    private void setRedTag(KnowledgeListViewHolder viewHolder, @StringRes int tagName) {
            viewHolder.getView(R.id.item_pager_tag_red_tv).setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.item_pager_tag_red_tv, tagName);
            viewHolder.setTextColor(R.id.item_pager_tag_red_tv, ContextCompat.getColor(mContext, R.color.light_deep_red));
            viewHolder.setBackgroundRes(R.id.item_pager_tag_red_tv, R.drawable.selector_tag_red_background);
    }
}
