package com.davy.davy_wanandroid.di.module;

import com.davy.davy_wanandroid.BuildConfig;
import com.davy.davy_wanandroid.app.Constants;
import com.davy.davy_wanandroid.app.WanAndroidApplication;
import com.davy.davy_wanandroid.core.http.api.WanAndroidApi;
import com.davy.davy_wanandroid.di.qualifier.WanAndroidUrl;
import com.davy.davy_wanandroid.utils.CommonUtils;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author: Davy
 * date: 18/9/17
 */
@Module
public class HttpModule {

    @Singleton
    @Provides
    WanAndroidApi provideWanAndroidApi (@WanAndroidUrl Retrofit retrofit){
        return retrofit.create(WanAndroidApi.class);
    }

    @Singleton
    @Provides
    @WanAndroidUrl
    Retrofit provideRetrofit (Retrofit.Builder builder,OkHttpClient client){
        return createRetrofit(builder,client,WanAndroidApi.HOST);
    }

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder(){
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkhttpClientBuilder(){
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkhttpClient(OkHttpClient.Builder builder){
        if(BuildConfig.DEBUG){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }
        File cacheFile = new File(Constants.PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if(!CommonUtils.isNetWorkConnected()){
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if(CommonUtils.isNetWorkConnected()){
                    int maxAge = 0;
                    //有网络时，不缓存
                    response.newBuilder()
                            .header("Cache-Control","public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                }else{
                    //无网络时，设置超时时间为2周
                    int maxStale = 60 * 60 * 24 * 14;
                    response.newBuilder()
                            .header("Cache-Control","public, only-if-cached, max-stale" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }

                return response;
            }
        };

        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20,TimeUnit.SECONDS);
        builder.writeTimeout(20,TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        //Cookie持久化
        builder.cookieJar(new PersistentCookieJar(new SetCookieCache(),new SharedPrefsCookiePersistor(WanAndroidApplication.getInstance())));
            return builder.build();
    }

    private Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client, String url) {
        return builder
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }
}
