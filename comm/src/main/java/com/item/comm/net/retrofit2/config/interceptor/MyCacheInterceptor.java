package com.item.comm.net.retrofit2.config.interceptor;

import com.item.comm.util.NetworkUtils2;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import project.utils.PLog;

/**
 * Created by Fracesuit on 2017/7/20.
 */

public class MyCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        PLog.e("interceptinterceptintercept");
        // 有网络时 设置缓存超时时间1个小时
        //int maxAge = 60 * 60;
        int maxAge = 30;
        // 无网络时，设置超时为1天
        int maxStale = 60 * 60 * 24;
        Request request = chain.request();
        if (NetworkUtils2.isNetworkAvailable()) {
            //有网络时只从网络获取
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();
        } else {
            //无网络时只从缓存中读取
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }


        Response response = chain.proceed(request);
        if (NetworkUtils2.isNetworkAvailable()) {
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return response;
    }
}
