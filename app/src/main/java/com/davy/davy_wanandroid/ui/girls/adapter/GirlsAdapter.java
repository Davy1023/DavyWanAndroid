package com.davy.davy_wanandroid.ui.girls.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.bean.girls.GirlsImageData;
import com.davy.davy_wanandroid.bean.girls.PicSizeData;
import com.davy.davy_wanandroid.ui.girls.viewholder.GirlsViewHolder;
import com.davy.davy_wanandroid.utils.CommonUtils;

import java.util.List;

/**
 * author: Davy
 * date: 2018/10/12
 */
public class GirlsAdapter extends BaseQuickAdapter<GirlsImageData, GirlsViewHolder> {

    private final RequestOptions mOptions;
    private int screenWidth;
    private ArrayMap<String, PicSizeData> picSizeArrayMap = new ArrayMap<>();

    public GirlsAdapter(int layoutResId, @Nullable List<GirlsImageData> data) {
        super(layoutResId, data);
        screenWidth = CommonUtils.getWidth(WanAndroidApplication.getInstance());
        mOptions = new RequestOptions();
        mOptions.fitCenter();
        mOptions.placeholder(R.drawable.icon_pic_gray_bg);
        mOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    protected void convert(final GirlsViewHolder viewHolder, final GirlsImageData item) {
        final RelativeLayout mLayout = viewHolder.getView(R.id.rl_root);
        ImageView mIvGirls = viewHolder.getView(R.id.iv_girls);

        Glide.with(mContext)
                .asBitmap()
                .load(item.getUrl())
                .apply(mOptions)
                .thumbnail(0.2f)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        PicSizeData picSizeData = picSizeArrayMap.get(item.getUrl());
                        if(picSizeData == null || picSizeData.isNull()){
                            int width = resource.getWidth();
                            int height = resource.getHeight();
                            //计算高宽比
                            int finalHeight = (screenWidth / 2) * height / width;
                            ViewGroup.LayoutParams layoutParams = mLayout.getLayoutParams();
                            layoutParams.height = finalHeight;
                            picSizeArrayMap.put(item.getUrl(), new PicSizeData(width, height));
                        }

                        return false;
                    }
                })
                .into(mIvGirls);
    }
}
