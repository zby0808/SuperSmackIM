package com.item.comm;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.item.comm.net.retrofit2.config.RetrofitManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import okhttp3.OkHttpClient;

public abstract class BaseApplication extends MultiDexApplication {
    private static final String TAG = "BaseApplication";
    private boolean isDebug;
    protected RetrofitManager mRetrofitManager;
    private RefWatcher refWatcher;

    public BaseApplication(boolean isDebug) {
        this.isDebug = isDebug;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //方法数上限
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        CommInit.init(this, isDebug);
        super.onCreate();
//        if (isDebug) {
//            refWatcher = LeakCanary.install(this);//当正式发布的时候，删除内存泄漏-------
//        }
    }

    //内存泄漏检查，当正式发布的时候，删除内存泄漏-------
    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    protected <T> T createApiService(String baseUrl, OkHttpClient.Builder okBuilder, Class<T> clazz) {
        //网络
        mRetrofitManager = RetrofitManager.init(this, isDebug, baseUrl, true, okBuilder);
        return mRetrofitManager.createApiService(clazz);
    }
}
