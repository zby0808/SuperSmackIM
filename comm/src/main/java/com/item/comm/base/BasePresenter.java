package com.item.comm.base;


import com.item.comm.rxjava1.DefaultSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public  class BasePresenter {
    protected BaseView mView;

    protected void attach(BaseView view) {
        mView = view;
    }

    protected void destroy() {
        release();
        mView = null;
    }

    private Map<Integer, Subscription> subscriptionMap;

    protected <T> void execute(Observable<T> observable, final int requestCode) {
        final DefaultSubscriber<T> observer = new DefaultSubscriber<T>(mView, requestCode) {
            @Override
            public void onNext(T t) {
                mView.doParseData(requestCode, t);
            }
        };

        Subscription subscribe = observable
                .subscribeOn(Schedulers.io())//控制前面的
                .observeOn(AndroidSchedulers.mainThread())//控制后面的
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        observer.doOnCancel();//任务取消了
                    }
                })
                .compose(observer.bindLifecycle())//绑定生命周期
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        observer.doOnStart();//主线程中进行，也可以在子线程中进行，由后面的subscribeOn来确定
                    }
                })
                .subscribe(observer);

        addSubscription(requestCode, subscribe);
    }

    /**
     * 任务取消 释放资源
     */
    private void release() {
        if (subscriptionMap != null) {
            cancelAll();
            subscriptionMap.clear();
            subscriptionMap = null;
        }
    }

    public void cancel(Integer requestCode) {
        final Subscription subscription = subscriptionMap.get(requestCode);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public void cancelAll() {
        if (subscriptionMap != null) {
            final Set<Integer> integers = subscriptionMap.keySet();
            for (Integer key : integers) {
                cancel(key);
            }
        }
    }


    /**
     * 添加管理
     *
     * @param subscription
     */
    private void addSubscription(int requestCode, Subscription subscription) {
        if (subscriptionMap == null) {
            subscriptionMap = new HashMap();
        }
        subscriptionMap.put(requestCode, subscription);
    }
}
