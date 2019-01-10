package com.item.comm.rxjava1;


import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.Utils;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.item.comm.R;
import com.item.comm.base.BaseView;


import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import rx.Subscriber;

/**
 * Created by AppleRen on 2017/3/27.
 */

public abstract class DefaultSubscriber<T> extends Subscriber<T> {
    BaseView mView;
    int mRequestCode;

    public DefaultSubscriber(@NonNull BaseView view, int requestCode) {
        mView = view;
        mRequestCode = requestCode;
    }

    public void doOnStart()//开始
    {
        mView.doOnStart(mRequestCode);
    }

    public void doOnCancel()//取消
    {
        mView.doOnCancel(mRequestCode);
        mView = null;
    }

    public LifecycleTransformer<T> bindLifecycle()//绑定生命周期,获取泛型的实际类型
    {
        return mView.doBindLifecycle(mRequestCode);
    }

    public void doOnError(Throwable e, String msg)//错误的时候
    {
        Log.e("请求错误----", e.getMessage() + "-----" + msg);
        mView.doOnError(mRequestCode, msg, e);
    }

    @Override
    public void onCompleted() {
        mView.doOnCompleted(mRequestCode);
    }


    //Subscriber 的onstart不能更改线程，所以需要重写
    @Override
    public final void onError(Throwable e) {
        String error = null;
        if (e instanceof ConnectException) {
            error = Utils.getApp().getString(R.string.comm_net_error_connent);
        } else if (e instanceof SocketTimeoutException) {
            error = Utils.getApp().getString(R.string.comm_net_error_timeout);
        } else if (e instanceof HttpException) {
            switch (((HttpException) e).code()) {
                case 403:
                    error = Utils.getApp().getString(R.string.comm_net_error_403);
                    break;
                case 500:
                    error = Utils.getApp().getString(R.string.comm_net_error_500);
                    break;
                case 404:
                    error = Utils.getApp().getString(R.string.comm_net_error_nourl);
                    break;
                case 400://后端手动返回的异常
                    error = errorBack(e);
                    break;
                default:
                    error = Utils.getApp().getString(R.string.comm_net_error_http) + ((HttpException) e).code();
                    break;
            }
        } else {
            error = e.getMessage();
        }
        doOnError(e, error);
    }

    private String errorBack(Throwable e) {
        String error = "";
        ResponseBody responseBody = ((HttpException) e).response().errorBody();
        try {
            String s = responseBody.string();
            if (TextUtils.isEmpty(s)) {
                error = "后端服务异常";
            } else {
                JSONObject jsonObject = JSON.parseObject(s, JSONObject.class);
                String message = jsonObject.getString("message");
                error = message;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return error;
    }


}
