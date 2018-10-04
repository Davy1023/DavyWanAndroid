package com.davy.davy_wanandroid.presenter;

import com.davy.davy_wanandroid.R;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.base.presenter.BasePresenter;
import com.davy.davy_wanandroid.base.rx.BaseObsever;
import com.davy.davy_wanandroid.bean.BaseResponse;
import com.davy.davy_wanandroid.bean.main.BannerData;
import com.davy.davy_wanandroid.bean.main.LoginData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleData;
import com.davy.davy_wanandroid.bean.main.WanAndroidArticleListData;
import com.davy.davy_wanandroid.contract.mainpager.MainPagerContract;
import com.davy.davy_wanandroid.core.DataManager;
import com.davy.davy_wanandroid.core.event.CollectEvent;
import com.davy.davy_wanandroid.core.event.LoginEvent;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.davy.davy_wanandroid.utils.RxBus;
import com.davy.davy_wanandroid.utils.RxUtil;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Predicate;

/**
 * author: Davy
 * date: 18/10/1
 */
public class MainPagerPresenter extends BasePresenter<MainPagerContract.View> implements MainPagerContract.Presenter{

    private DataManager mDataManager;
    private boolean isRefresh = true;
    private int mCurrentPage;

    @Inject
    public MainPagerPresenter(DataManager dataManager) {
        super(dataManager);
        this.mDataManager = dataManager;
    }

    @Override
    public void attachView(MainPagerContract.View view) {
        super.attachView(view);
        registerEvent();
    }

    private void registerEvent() {
        addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
            .filter(new Predicate<CollectEvent>() {
                @Override
                public boolean test(CollectEvent collectEvent) throws Exception {
                    return !collectEvent.isCancelCollectSucces();
                }
            })
            .subscribe(new Consumer<CollectEvent>() {
                @Override
                public void accept(CollectEvent collectEvent) throws Exception {
                        mView.showCollectSucces();
                }
            })
        );

        addSubscribe(RxBus.getDefault().toFlowable(CollectEvent.class)
                .filter(new Predicate<CollectEvent>() {
                    @Override
                    public boolean test(CollectEvent collectEvent) throws Exception {
                        return collectEvent.isCancelCollectSucces();
                    }
                })
                .subscribe(new Consumer<CollectEvent>() {
                    @Override
                    public void accept(CollectEvent collectEvent) throws Exception {
                        mView.showCancleCollectSucces();
                    }
                })
        );

        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .filter(new Predicate<LoginEvent>() {
                    @Override
                    public boolean test(LoginEvent loginEvent) throws Exception {
                        return loginEvent.isLogin();
                    }
                })
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        mView.showLoginView();
                    }
                })
        );

        addSubscribe(RxBus.getDefault().toFlowable(LoginEvent.class)
                .filter(new Predicate<LoginEvent>() {
                    @Override
                    public boolean test(LoginEvent loginEvent) throws Exception {
                        return !loginEvent.isLogin();
                    }
                })
                .subscribe(new Consumer<LoginEvent>() {
                    @Override
                    public void accept(LoginEvent loginEvent) throws Exception {
                        mView.showLoginOutView();
                    }
                })
        );
    }

    @Override
    public String getLoginPassword() {
        return mDataManager.getLoginPassword();
    }

    @Override
    public String getLoginAccount() {
        return mDataManager.getLoginAccount();
    }

    @Override
    public void loadMainPagerData() {
        Observable<BaseResponse<LoginData>> mLoginObservable = mDataManager.getLoginData(getLoginAccount(), getLoginPassword());
        Observable<BaseResponse<List<BannerData>>> mBannerObservable = mDataManager.getBannerData();
        Observable<BaseResponse<WanAndroidArticleListData>> mArticleListObservable = mDataManager.getWanAndroidArticleListData(0);
        addSubscribe(Observable.zip(mLoginObservable, mBannerObservable, mArticleListObservable,
                new Function3<BaseResponse<LoginData>, BaseResponse<List<BannerData>>, BaseResponse<WanAndroidArticleListData>, HashMap<String, Object>>() {

                    @Override
                    public HashMap<String, Object> apply(BaseResponse<LoginData> loginResponse, BaseResponse<List<BannerData>> bannerResponse, BaseResponse<WanAndroidArticleListData> articleListResponse) throws Exception {
                        return createResponseMap(loginResponse, bannerResponse, articleListResponse);
                    }
                })
                .compose(RxUtil.<HashMap<String,Object>>rxScheduleHelper())
                .subscribeWith(new BaseObsever<HashMap<String, Object>>(mView){

                    @Override
                    public void onNext(HashMap<String, Object> map) {
                        BaseResponse<LoginData> loginResponse = CommonUtils.cast(map.get(Constants.LOGIN_DATA));
                        if(loginResponse.getErrorCode() == BaseResponse.SUCCESS){
                            loginSucces(loginResponse);
                        }else {
                            mView.showAutoLoginFail();
                        }

                        BaseResponse<List<BannerData>> bannerResponse = CommonUtils.cast(map.get(Constants.Banner_DATA));
                        if(bannerResponse != null){
                            mView.showBannerData(bannerResponse.getData());
                        }

                        BaseResponse<WanAndroidArticleListData> articleResponse= CommonUtils.cast(map.get(Constants.ARTICLE_LIST_DATA));
                        if(articleResponse != null){
                            mView.showArticleList(articleResponse.getData(), isRefresh);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.showAutoLoginFail();
                    }
                })
        );

    }

    private void loginSucces(BaseResponse<LoginData> loginResponse) {
        LoginData loginData = loginResponse.getData();
        mDataManager.setLoginAccount(loginData.getUsername());
        mDataManager.setLoginPassword(loginData.getPassword());
        mDataManager.setLoginStatus(true);
        mView.showAutoLoginSuccess();
    }

    private HashMap<String,Object> createResponseMap(BaseResponse<LoginData> loginResponse, BaseResponse<List<BannerData>> bannerResponse, BaseResponse<WanAndroidArticleListData> articleListResponse) {
        HashMap<String, Object> map = new HashMap<>(3);
        map.put(Constants.LOGIN_DATA, loginResponse);
        map.put(Constants.Banner_DATA, bannerResponse);
        map.put(Constants.ARTICLE_LIST_DATA, articleListResponse);
        return map;

    }

    @Override
    public void getWanAndroidArticleList(boolean isShowError) {
        addSubscribe(mDataManager.getWanAndroidArticleListData(mCurrentPage)
                .compose(RxUtil.<BaseResponse<WanAndroidArticleListData>>rxScheduleHelper())
                .compose(RxUtil.<WanAndroidArticleListData>handleResult())
                .filter(new Predicate<WanAndroidArticleListData>() {
                    @Override
                    public boolean test(WanAndroidArticleListData wanAndroidArticleListData) throws Exception {
                        return mView != null;
                    }
                })
                .subscribeWith(new BaseObsever<WanAndroidArticleListData>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_article_data), isShowError) {
                    @Override
                    public void onNext(WanAndroidArticleListData wanAndroidArticleListData) {
                        mView.showArticleList(wanAndroidArticleListData,isRefresh);
                    }
                })
        );
    }

    @Override
    public void loadMoreData() {
        addSubscribe(mDataManager.getWanAndroidArticleListData(mCurrentPage)
                .compose(RxUtil.<BaseResponse<WanAndroidArticleListData>>rxScheduleHelper())
                .compose(RxUtil.<WanAndroidArticleListData>handleResult())
                .filter(new Predicate<WanAndroidArticleListData>() {
                    @Override
                    public boolean test(WanAndroidArticleListData wanAndroidArticleListData) throws Exception {
                        return mView != null;
                    }
                })
                .subscribeWith(new BaseObsever<WanAndroidArticleListData>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_article_data), false) {
                    @Override
                    public void onNext(WanAndroidArticleListData wanAndroidArticleListData) {
                        mView.showArticleList(wanAndroidArticleListData, isRefresh);
                    }
                })
        );
    }

    @Override
    public void addCollectArticle(final int position, final WanAndroidArticleData wanAndroidArticleData) {
        addSubscribe(mDataManager.addCollectArticle(wanAndroidArticleData.getId())
                .compose(RxUtil.<BaseResponse<WanAndroidArticleListData>>rxScheduleHelper())
                .compose(RxUtil.<WanAndroidArticleListData>hanleCollteResult())
                .subscribeWith(new BaseObsever<WanAndroidArticleListData>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_collect)) {
                    @Override
                    public void onNext(WanAndroidArticleListData wanAndroidArticleListData) {
                        wanAndroidArticleData.setCollect(true);
                        mView.showCollectArticleData(position, wanAndroidArticleData, wanAndroidArticleListData);
                    }
                })
        );

    }

    @Override
    public void cancelCollectArticle(final int position, final WanAndroidArticleData wanAndroidArticleData) {
        addSubscribe(mDataManager.cancelCollectArticle(wanAndroidArticleData.getId())
                .compose(RxUtil.<BaseResponse<WanAndroidArticleListData>>rxScheduleHelper())
                .compose(RxUtil.<WanAndroidArticleListData>hanleCollteResult())
                .subscribeWith(new BaseObsever<WanAndroidArticleListData>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_cancle_collect)) {
                    @Override
                    public void onNext(WanAndroidArticleListData wanAndroidArticleListData) {
                        wanAndroidArticleData.setCollect(false);
                        mView.showCancleCollectArticleData(position, wanAndroidArticleData, wanAndroidArticleListData);
                    }
                })
        );

    }

    @Override
    public void getBannerData(boolean isShowError) {
        addSubscribe(mDataManager.getBannerData()
                .compose(RxUtil.<BaseResponse<List<BannerData>>>rxScheduleHelper())
                .compose(RxUtil.<List<BannerData>>handleResult())
                .subscribeWith(new BaseObsever<List<BannerData>>(mView, WanAndroidApplication.getInstance().getString(R.string.fail_banner_data), isShowError) {
                    @Override
                    public void onNext(List<BannerData> bannerDataList) {
                        mView.showBannerData(bannerDataList);
                    }
                })
        );
    }

    @Override
    public void autoRefresh(boolean isShowError) {
        isRefresh = true;
        mCurrentPage = 0;
        getBannerData(isShowError);
        getWanAndroidArticleList(isShowError);

    }

    @Override
    public void loadMore() {
        isRefresh = false;
        mCurrentPage ++;
        loadMoreData();
    }
}
