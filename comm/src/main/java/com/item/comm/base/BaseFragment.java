package com.item.comm.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.item.comm.constant.NetRequestCode;
import com.item.comm.help.DialogHelp;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import project.utils.PLog;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements LifecycleProvider<FragmentEvent>, BaseView {
    public final String TAG = this.getClass().getSimpleName();
    protected P mPresenter;
    Unbinder unbinder;
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();
    protected AppCompatActivity activity;

    private boolean isActivityCreate = false;
    private boolean isFirstLoadData = true;

    protected boolean isLasy = false;
    protected boolean isRefreshAlways = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setupMvp();
        activity = (AppCompatActivity) getActivity();
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutId(), null);
        unbinder = ButterKnife.bind(this, view);
        onCreateViewAfter(view, savedInstanceState);
        if (!isLasy && getUserVisibleHint() && isFirstLoadData) {
            isFirstLoadData = false;
            requestData();
        }
        return view;
    }

    protected abstract void requestData();

    /**
     * 这个方法只有在移除出屏幕的时候才会调用。并不是真正意义上的不可见
     * 估计这个方法只有viewpager主动调用
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isActivityCreate) {
            if (isVisibleToUser) {
                visible();
                if (isFirstLoadData && isRefreshAlways) {//刷新
                    isFirstLoadData = false;
                    requestData();
                }
            } else {
                inVisible();
                if (isRefreshAlways) {
                    isFirstLoadData = true;
                }
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void visible() {
    }

    protected void inVisible() {
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isActivityCreate = true;
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void onCreateViewAfter(View view, Bundle savedInstanceState);


    private void setupMvp() {
        mPresenter = getPresenter();
        mPresenter.attach(this);
    }

    protected abstract P getPresenter();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
        if (isFirstLoadData && isRefreshAlways) {//刷新
            isFirstLoadData = false;
            requestData();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        if (isRefreshAlways) {
            isFirstLoadData = true;
        }
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }


    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        mPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }


    //==========================等待框start==========================
    MaterialDialog mProgressDialog;

    protected void showProgressBar(int requestCode) {
        showProgressBar(requestCode, true);
    }

    protected void showProgressBar(final int requestCode, boolean cancel) {
        if (mProgressDialog == null) {
            DialogInterface.OnCancelListener onCancelListener = null;
            if (cancel) {
                onCancelListener = new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancelTask(requestCode);
                    }
                };
            }
            mProgressDialog = DialogHelp.createProgressDialog(activity, onCancelListener);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    protected void cancelTask(int requestCode) {
        PLog.d(TAG + "cancelTask");
    }

    public void hideProgressBar() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }
    //==========================等待框end==========================

    //==========================等待框end==========================

    //========================rx相关start===============================
    @Override
    public void doOnStart(int requestCode) {
        showProgressBar(requestCode);
        PLog.d("任务开始了");
    }

    @Override
    public void doOnCancel(int requestCode) {
        hideProgressBar();
        PLog.d("任务释放了");
    }

    @Override
    public void doOnError(@NetRequestCode int requestCode, String msg, Throwable e) {
        hideProgressBar();
        Toasty.error(activity, msg, Toast.LENGTH_SHORT).show();
        PLog.d("任务出现错误了");
    }

    @Override
    public void doOnCompleted(int requestCode) {
        hideProgressBar();
        PLog.d("任务完成了");
    }
    //========================rx相关end===============================

    //========================activity相关start================R===============


    @Override
    public LifecycleTransformer doBindLifecycle(int requestCode) {
        return this.bindToLifecycle();
    }

    public void setLasy(boolean lasy) {
        isLasy = lasy;
    }

    public void setRefreshAlways(boolean refreshAlways) {
        isRefreshAlways = refreshAlways;
    }
}
