package myproject.smack.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.allen.library.SuperButton;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.item.comm.base.BaseActivity;
import com.item.comm.constant.NetRequestCode;
import com.item.comm.util.IntentUtils2;
import com.item.comm.util.ToastyUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import myproject.MainXYSpHelper;
import myproject.MyApplication;
import myproject.R;
import myproject.R2;
import myproject.bean.LoginUserBean;
import myproject.presenter.LoginPresent;
import project.utils.PLog;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *登录窗口
 * Created by zby on 2018/11/29.
 */
public class LoginActivity extends BaseActivity<LoginPresent> {

    public static final int PERMISSION_REQUEST_CODE = 5678;

    @BindView(R2.id.edt_UsrName)
    EditText edtUsrName;
    @BindView(R2.id.edt_UsrPwd)
    EditText edtUsrPwd;
    @BindView(R2.id.btn_foeget_pw)
    TextView btnForgetPw;
    @BindView(R2.id.tv_version)
    TextView tvVersion;
    @BindView(R2.id.btn_Login)
    SuperButton mLoginBtn;

    @Override
    public void doParseData(int requestCode, Object data) {
        if (requestCode == NetRequestCode.NET_REQUEST_1) {
            if (data != null) {
                //初始化文件路径,所有的都放到同一个用户下
//                FileUtils2.init(UserManager.getAppUserModel().getUserName());
                ActivityUtils.startActivity(this, MainActivity.class);
                finish();
            } else {
                ToastyUtil.warningShort(getString(R.string.login_state_msg));
            }
        }
    }

    @Override
    protected void onCreateAfter(Bundle savedInstanceState) {
        MainXYSpHelper helper = MainXYSpHelper.getInstance(Utils.getApp());
        String userNameValue = helper.getUserName();
        String pwdValue = helper.getPassWord();
        if (!TextUtils.isEmpty(userNameValue)) {
            edtUsrName.setText(userNameValue);
            edtUsrPwd.setText(pwdValue);
        }

        String userName = edtUsrName.getText().toString();
        if (!TextUtils.isEmpty(userName)) edtUsrName.setSelection(userName.length());
        String localVersionName = getLocalVersionName();
        tvVersion.setText("当前版本:" + localVersionName);

        //动态申请权限
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            //当所有权限都允许之后，返回true
                            Log.i("permissions", "申请权限成功：" + aBoolean);
                        } else {
                            //只要有一个权限禁止，返回false，
                            //下一次申请只申请没通过申请的权限
                            Log.i("permissions", "申请权限失败：" + aBoolean);
                            showPermissionDialog();
                        }
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresent getPresenter() {
        return new LoginPresent();
    }

    @Override
    public void doOnStart(int requestCode) {
        super.doOnStart(requestCode);
    }

    @OnClick({R2.id.btn_Login, R2.id.btn_foeget_pw})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_Login){
            login();
//                if (verifyParam())
//                    mPresenter.login(edtUsrName.getText().toString().trim(),
//                            edtUsrPwd.getText().toString().trim(),
//                            NetRequestCode.NET_REQUEST_1);
        }else if (view.getId()==R.id.btn_foeget_pw){
            // DialogHelp.showForgetPwdDialog(this);

        }
    }

    Map<String, Object> params = new HashMap<>();

    private boolean verifyParam() {
        String account = edtUsrName.getText().toString().trim();
        String pwd = edtUsrPwd.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toasty.warning(this, getString(R.string.login_account_msg), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(pwd)) {
            Toasty.warning(this, getString(R.string.login_pwd_msg), Toast.LENGTH_SHORT).show();
            return false;
        }
        params.put("username", account);
        params.put("password", pwd);
        return true;
    }

    public String getLocalVersionName() {
        String localVersion = "";
        try {
            PackageInfo packageInfo = getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
            localVersion = packageInfo.versionName;
            PLog.d("本软件的版本" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    private void showPermissionDialog() {
        new MaterialDialog.Builder(this)
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
                        final Intent appDetailsSettingsIntent = IntentUtils2.getAppDetailsSettingsIntent(getPackageName());
                        startActivityForResult(appDetailsSettingsIntent, PERMISSION_REQUEST_CODE);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .show();
    }

    public void login() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", edtUsrName.getText().toString().trim());
        jsonObject.put("password", edtUsrPwd.getText().toString().trim());
        Observable<LoginUserBean> observable = MyApplication.superApi.login(jsonObject);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoginUserBean>() {
                    @Override
                    public void call(LoginUserBean bean) {
                        MainXYSpHelper helper = MainXYSpHelper.getInstance(Utils.getApp());
                        helper.saveToken(bean.getToken());
                        helper.saveUserName(edtUsrName.getText().toString().trim());
                        helper.savePassWord(edtUsrPwd.getText().toString().trim());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                });
    }
}
