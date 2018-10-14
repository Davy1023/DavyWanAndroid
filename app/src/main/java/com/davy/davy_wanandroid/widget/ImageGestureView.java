package com.davy.davy_wanandroid.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

/**
 * author: Davy
 * date: 18/10/12
 */
public class ImageGestureView extends RelativeLayout {

    public ImageGestureView(Context context) {
        this(context, null);
    }

    public ImageGestureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageGestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static final int mHeight = 500;
    private float mDisplacementX;
    private float mDisplacementY;
    private float mInitialTy;
    private float mInitialTx;
    private boolean mTracking;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                mDisplacementX = event.getRawX();
                mDisplacementY = event.getRawY();

                mInitialTy = getTranslationY();
                mInitialTx = getTranslationX();

                break;

            case MotionEvent.ACTION_MOVE:
                // get the delta distance in X and Y direction
                float deltaX = event.getRawX() - mDisplacementX;
                float deltaY = event.getRawY() - mDisplacementY;


                // set the touch and cancel event
                if (deltaY > 0 && (Math.abs(deltaY) > ViewConfiguration.get(getContext()).getScaledTouchSlop() * 2 && Math.abs(deltaX) < Math.abs(deltaY) / 2)
                        || mTracking) {

                    onSwipeListener.onSwiping(deltaY);

                    setBackgroundColor(Color.TRANSPARENT);

                    mTracking = true;

                    setTranslationY(mInitialTy + deltaY);
                    setTranslationX(mInitialTx + deltaX);

                    float mScale = 1 - deltaY / mHeight;
                    if (mScale < 0.3) {
                        mScale = 0.3f;
                    }
                    setScaleX(mScale);
                    setScaleY(mScale);

                }
                if (deltaY < 0) {
                    setViewDefault();
                }

                break;

            case MotionEvent.ACTION_UP:

                if (mTracking) {
                    mTracking = false;
                    float currentTranslateY = getTranslationY();

                    if (currentTranslateY > mHeight / 3) {
                        onSwipeListener.downSwipe();
                        break;
                    }

                }
                setViewDefault();

                onSwipeListener.overSwipe();
                break;
        }
        return false;
    }

    private void setViewDefault(){
        //恢复默认
        setAlpha(1);
        setTranslationX(0);
        setTranslationY(0);
        setScaleX(1);
        setScaleY(1);
        setBackgroundColor(Color.BLACK);
    }


    public interface OnSwipeListener {
        //向下滑动
        void downSwipe();

        //结束
        void overSwipe();

        //正在滑动
        void onSwiping(float y);
    }


    private OnSwipeListener onSwipeListener;

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

}
