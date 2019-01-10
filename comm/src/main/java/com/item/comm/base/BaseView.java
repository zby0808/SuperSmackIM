package com.item.comm.base;


import com.trello.rxlifecycle.LifecycleTransformer;
import com.item.comm.constant.NetRequestCode;


/**
 * MVPPlugin
 */

public interface BaseView  {
    void doOnStart(@NetRequestCode int requestCode);

    void doOnCancel(@NetRequestCode int requestCode);

    void doOnError(@NetRequestCode int requestCode, String msg, Throwable e);

    void doOnCompleted(@NetRequestCode int requestCode);

    LifecycleTransformer doBindLifecycle(@NetRequestCode int requestCode);

    void doParseData(@NetRequestCode int requestCode, Object data);
}
