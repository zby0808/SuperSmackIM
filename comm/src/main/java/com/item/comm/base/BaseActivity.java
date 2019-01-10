package com.item.comm.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.item.comm.constant.NetRequestCode;
import com.item.comm.help.DialogHelp;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import project.utils.PLog;
import rx.Observable;
import rx.subjects.BehaviorSubject;


public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements LifecycleProvider<ActivityEvent>, BaseView {
    public final String TAG = this.getClass().getSimpleName();
    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupMvp();
        onCreateBefore(savedInstanceState);
        super.onCreate(savedInstanceState);
        PLog.d(TAG + "onCreate");
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        //设置布局内容
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        //初始化操作
        onCreateAfter(savedInstanceState);
    }

    protected void onCreateBefore(Bundle savedInstanceState) {
    }

    protected abstract void onCreateAfter(Bundle savedInstanceState);


    @LayoutRes
    protected abstract int getLayoutId();


    private void setupMvp() {
        mPresenter = getPresenter();
        mPresenter.attach(this);
    }

    protected abstract P getPresenter();


    //rx生命周期管控
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <P> LifecycleTransformer<P> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <P> LifecycleTransformer<P> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
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
            mProgressDialog = DialogHelp.createProgressDialog(this, onCancelListener);
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
    public LifecycleTransformer doBindLifecycle(int requestCode) {
        return this.bindToLifecycle();
    }



    @Override
    public void doOnError(@NetRequestCode int requestCode, String msg, Throwable e) {
        hideProgressBar();
        Toasty.error(this, msg, Toast.LENGTH_SHORT).show();
        PLog.d("任务出现错误了");
    }

    @Override
    public void doOnCompleted(int requestCode) {
        hideProgressBar();
        PLog.d("任务完成了");
    }
    //========================rx相关end===============================

    //========================activity相关start===============================


    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
        PLog.d(TAG + "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        PLog.d(TAG + "onResume");
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
        PLog.d(TAG + "onPause");
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
        PLog.d(TAG + "onStop");
    }

    @Override
    protected void onDestroy() {
        PLog.d(TAG + "onDestroy");
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        mPresenter.destroy();
        super.onDestroy();
    }

    //========================activity相关end===============================


}
