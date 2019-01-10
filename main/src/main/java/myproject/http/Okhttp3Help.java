package myproject.http;

import com.blankj.utilcode.util.Utils;

import okhttp3.OkHttpClient;

/**
 * Created by zhiren.zhang on 2017/10/16.
 */

public class Okhttp3Help {

    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HeadInterceptor());
        builder.addInterceptor(new ReLoginInterceptor(Utils.getApp()));
        return builder;
    }
}
