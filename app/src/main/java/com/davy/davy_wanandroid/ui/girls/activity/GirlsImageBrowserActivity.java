package com.davy.davy_wanandroid.ui.girls.activity;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.utils.BitmapUtils;
import com.davy.davy_wanandroid.utils.PermissionUtils;
import com.davy.davy_wanandroid.utils.ZoomOutPageTransformer;
import com.davy.davy_wanandroid.widget.ImageGestureView;
import com.davy.davy_wanandroid.widget.dialog.ListFragmentDialog;
import com.davy.davy_wanandroid.widget.dialog.OnItemClickListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.maning.mndialoglibrary.MProgressDialog;
import com.maning.mndialoglibrary.MStatusDialog;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Davy
 * date: 18/10/12
 */
public class GirlsImageBrowserActivity extends AppCompatActivity {

    @BindView(R.id.rl_black_bg)
    RelativeLayout mRlBlackBg;
    @BindView(R.id.viewPagerBrowser)
    ViewPager mViewPager;
    @BindView(R.id.GestureView)
    ImageGestureView mGestureView;
    @BindView(R.id.tvNumShow)
    TextView mTvNumShow;
    @BindView(R.id.browser_root)
    RelativeLayout mBrowserRoot;

    private Context mContext;
    private ArrayList<String> mImageList;
    private int mCurrentPosition;

    private ImageView mCurrentImageView;
    private int mClickPositon;

    private ArrayList<String> mListDialogData = new ArrayList<>();

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setWindowFullScreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        ButterKnife.bind(this);

        mContext = this;

        initIntent();

        initData();

        initViewPager();


    }

    private void setWindowFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= 19) {
            //虚拟导航栏透明
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

    }

    private void initIntent() {
        mImageList = getIntent().getStringArrayListExtra(Constants.IMAGELIST);
        mCurrentPosition = getIntent().getIntExtra(Constants.CURRENT_POSITION, 0);
    }

    private void initData() {
        mTvNumShow.setText(String.valueOf(mCurrentPosition + 1) + "/" + mImageList.size());
    }

    private void initViewPager() {
        mViewPager.setAdapter(new ImageAdapter());
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                mTvNumShow.setText(String.valueOf(position + 1) + "/" + mImageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mGestureView.setOnSwipeListener(new ImageGestureView.OnSwipeListener() {
            @Override
            public void downSwipe() {
                finishBrowser();
            }

            @Override
            public void overSwipe() {
                mTvNumShow.setVisibility(View.VISIBLE);
                mRlBlackBg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSwiping(float deltaY) {
                mTvNumShow.setVisibility(View.GONE);

                float mAlpha = 1 - deltaY / 500;
                if (mAlpha < 0.3) {
                    mAlpha = 0.3f;
                }
                if (mAlpha > 1) {
                    mAlpha = 1;
                }
                mRlBlackBg.setAlpha(mAlpha);
            }
        });



    }

    private class ImageAdapter extends PagerAdapter {

        private LayoutInflater mInflater;
        private RequestOptions mOptions;

        public ImageAdapter() {

            mInflater = LayoutInflater.from(mContext);

            mOptions = new RequestOptions();
            mOptions.placeholder(R.drawable.icon_pic_gray_bg);
            mOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = mInflater.inflate(R.layout.item_browser_image, container, false);
            final PhotoView mPhotoView = view.findViewById(R.id.photoImageView);
            final ImageView mIvFail = view.findViewById(R.id.iv_fail);
            final RelativeLayout mPlaceholderBg = view.findViewById(R.id.rl_image_placeholder_bg);
            RelativeLayout mBrowserRoot = view.findViewById(R.id.rl_browser_root);

            mIvFail.setVisibility(View.GONE);

            final String url = mImageList.get(position);

            Glide.with(mContext)
                    .load(url)
                    .apply(mOptions)
                    .thumbnail(0.2f)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mIvFail.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mPlaceholderBg.setVisibility(View.GONE);
                            mIvFail.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(mPhotoView);

            mBrowserRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishBrowser();
                }
            });

            mPhotoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishBrowser();
                }
            });

            mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                  mClickPositon = position;
                  mCurrentImageView = mPhotoView;
                  showBottomDialogSheet();
                    return false;
                }
            });

            container.addView(view);
            return view;
        }
    }

    private void showBottomDialogSheet() {
        mListDialogData.clear();
        mListDialogData.add(getString(R.string.image_save));
        mListDialogData.add(getString(R.string.image_share));
        mListDialogData.add(getString(R.string.set_wallpaper));

        ListFragmentDialog.getInstance(this).showDialog(getSupportFragmentManager(), mListDialogData, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position == 0){
                    //保存图片
                    PermissionUtils.checkWritePermission(mContext, new PermissionUtils.PermissionCallBack() {
                        @Override
                        public void onGranted() {
                            saveImage();
                        }

                        @Override
                        public void onDenied() {
                            showProgressError(getString(R.string.fail_write_permission));
                        }
                    });
                }else if(position == 1){
                    startAppShare(getString(R.string.wanAndroid_image_share), getString(R.string.girls_image_share) + mImageList.get(mClickPositon));
                }else if(position == 2){
                    setWallpager();
                }
            }
        });

    }

    private void setWallpager() {
        showProgressDialog(getString(R.string.setting_wallpager));
        mCurrentImageView.setDrawingCacheEnabled(true);
        final Bitmap bitmap = Bitmap.createBitmap(mCurrentImageView.getDrawingCache());
        mCurrentImageView.setDrawingCacheEnabled(false);

        if(bitmap == null){
            showProgressError(getString(R.string.fail_setting));
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag = false;
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
                try {
                    wallpaperManager.setBitmap(bitmap);
                    flag = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    flag = false;
                }finally {
                    if(flag){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dissmissProgressDialog();
                                showProgressSuccess(getString(R.string.setting_success));
                            }
                        });
                    }else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dissmissProgressDialog();
                                showProgressError(getString(R.string.fail_setting));
                            }
                        });
                    }
                }
            }
        }).start();

    }

    private void startAppShare(String shareTitle, String shareContent) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TITLE, shareTitle);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
        mContext.startActivity(Intent.createChooser(shareIntent, getString(R.string.share_go)));

    }

    private void saveImage() {
        showProgressDialog(getString(R.string.image_saving));
        mCurrentImageView.setDrawingCacheEnabled(true);
        final Bitmap bitmap = Bitmap.createBitmap(mCurrentImageView.getDrawingCache());
        mCurrentImageView.setDrawingCacheEnabled(false);
        if(bitmap == null){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean saveBitmapToSD = BitmapUtils.saveBitmapToSD(bitmap, Constants.IMAGE_PATH, System.currentTimeMillis() + ".jpg", true);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dissmissProgressDialog();
                        if(saveBitmapToSD){
                            showProgressSuccess(getString(R.string.save_image_success));
                        }else {
                            showProgressError(getString(R.string.save_image_fail));
                        }

                    }
                });
            }
        }).start();
    }

    private void showProgressSuccess(String message) {
        new MStatusDialog(mContext).show(message, getResources().getDrawable(R.drawable.icon_dialog_success));
    }

    private void showProgressError(String message) {
        new MStatusDialog(mContext).show(message, getResources().getDrawable(R.drawable.icon_dialog_fail));
    }

    private void dissmissProgressDialog() {
        MProgressDialog.dismissProgress();
    }

    private void showProgressDialog(String message) {
        MProgressDialog.showProgress(mContext, message);
    }

    @Override
    public void onBackPressed() {
        finishBrowser();
    }

    private void finishBrowser() {
        mTvNumShow.setVisibility(View.GONE);
        mRlBlackBg.setAlpha(0);
        finish();
        this.overridePendingTransition(0, R.anim.browser_exit_anim);
    }
}
