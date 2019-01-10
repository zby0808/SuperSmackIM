package com.item.comm.net.retrofit2.config;


import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.item.comm.net.retrofit2.config.converter.NullOnEmptyConverterFactory;
import com.item.comm.util.FileUtils2;

import java.util.concurrent.TimeUnit;

import me.jessyan.progressmanager.ProgressManager;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Fracesuit on 2017/5/22.
 */

public class RetrofitManager {
    private static RetrofitManager retrofitManager;
    private OkHttpClient mOkHttpClient;
    private Retrofit retrofit;

    public <T> T createApiService(Class<T> tClass) {
        return retrofit.create(tClass);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private RetrofitManager(Context context, boolean isDebug, String baseUrl, boolean isWatch, OkHttpClient.Builder okBuilder) {
        //网络调试
        if (isDebug) {
            Stetho.initializeWithDefaults(context);
        }
        initRetrofit(isDebug, baseUrl, isWatch, okBuilder);
    }

    public static RetrofitManager init(Context context, boolean isDebug, String baseUrl, boolean isWatch, OkHttpClient.Builder okBuilder) {
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager(context, isDebug, baseUrl, isWatch, okBuilder);
        }
        return retrofitManager;
    }

    public static RetrofitManager init(Context context, boolean isDebug, String baseUrl, boolean isWatch) {
        if (retrofitManager == null) {
            retrofitManager = init(context, isDebug, baseUrl, isWatch, null);
        }
        return retrofitManager;
    }


    private void initRetrofit(boolean isDebug, final String baseUrl, boolean isWatch, OkHttpClient.Builder okBuilder) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        StethoInterceptor stethoInterceptor = new StethoInterceptor();
        //MyCacheInterceptor cacheInterceptor = new MyCacheInterceptor();
//        Cache cache = new Cache(FileUtils2.getNetCacheDir(), 1024 * 1024 * 10);//缓存位置和大小
        if (okBuilder == null) {
            okBuilder = new OkHttpClient.Builder();
        }
        okBuilder
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);
//                .cache(cache);
        //.addNetworkInterceptor(cacheInterceptor);


        if (isDebug) {
            okBuilder.addInterceptor(loggingInterceptor);
            okBuilder.addNetworkInterceptor(stethoInterceptor);
        }

        //加入进度监听
        if (isWatch) {
            okBuilder = ProgressManager.getInstance().with(okBuilder);
        }
        mOkHttpClient = okBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .build();
    }
}
