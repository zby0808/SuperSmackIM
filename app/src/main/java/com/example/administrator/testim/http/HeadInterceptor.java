package com.example.administrator.testim.http;

import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;
import com.example.administrator.testim.Constants;
import com.example.administrator.testim.XYSpHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder();

        XYSpHelper helper = XYSpHelper.getInstance(Utils.getApp());

        if (helper != null) {
            if (!TextUtils.isEmpty(helper.getToken())) {
                requestBuilder.addHeader(Constants.TOKEN_KEY, helper.getToken());
            }

        }

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
