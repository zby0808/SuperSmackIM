package com.item.comm.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.item.comm.R;
import com.item.comm.util.IntentUtils2;

import rx.Subscriber;

/**
 * Created by Fracesuit on 2017/6/13.
 */

public class PermissionsHelp {
    //跳转到请求权限的请求码
    public static final int PERMISSION_REQUEST_CODE = 5678;

    /**
     * 权限列表
     */

    //存储
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    //位置
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;//gps定位权限
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;//基站定位权限
    //相机
    public static final String CAMERA = Manifest.permission.CAMERA;
    //打电话
    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;


    private Activity activity;
    private RxPermissions rxPermissions;

    public PermissionsHelp(@NonNull Activity activity) {
        this.activity = activity;
        rxPermissions = new RxPermissions(activity);
    }

    private PermissionsHelp() {
        throw new RuntimeException("必须使用带有一个参数的构造方法");
    }

    public void requestPermissions(@NonNull String... permissions) {
        requestPermissions(null, permissions);
    }

    public void requestPermissions(final OnRequestPermissionsListener onRequestPermissionsListener, @NonNull String... permissions) {
        rxPermissions.request(permissions)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        if (onRequestPermissionsListener != null) {
                            onRequestPermissionsListener.completed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (onRequestPermissionsListener != null) {
                            onRequestPermissionsListener.error(e);
                        }
                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if (onRequestPermissionsListener != null) {
                            if (granted) {
                                onRequestPermissionsListener.grant();
                            } else {
                                showPermissonDialog(onRequestPermissionsListener);
                            }
                        }

                    }
                });
    }

    private void showPermissonDialog(final OnRequestPermissionsListener onRequestPermissionsListener) {
        new MaterialDialog.Builder(activity)
                .title(R.string.comm_permission_title)
                .content(R.string.comm_permission_message_permission_failed)
                .positiveText(R.string.comm_permission_confirm)
                .autoDismiss(true)
                .negativeText(R.string.comm_permission_cancel)
                .canceledOnTouchOutside(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //跳转到权限设置界面
                        final Intent appDetailsSettingsIntent = IntentUtils2.getAppDetailsSettingsIntent(activity.getPackageName());
                        activity.startActivityForResult(appDetailsSettingsIntent, PERMISSION_REQUEST_CODE);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onRequestPermissionsListener != null) {
                            onRequestPermissionsListener.refuse();
                        }
                    }
                })
                .show();
    }
}
