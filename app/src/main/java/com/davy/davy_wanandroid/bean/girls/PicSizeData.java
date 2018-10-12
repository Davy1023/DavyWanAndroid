package com.davy.davy_wanandroid.bean.girls;

/**
 * author: Davy
 * date: 2018/10/12
 *
 *妹子福利加载图片缓存宽高，防止出现滑动图片改变高度问题
 */
public class PicSizeData {

    private String picUrl;
    private int picWidth;
    private int picHeight;

    public PicSizeData(int picWidth, int picHeight) {
        this.picWidth = picWidth;
        this.picHeight = picHeight;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public int getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }

    public boolean isNull() {
        return picHeight == 0 || picWidth == 0;
    }
}
