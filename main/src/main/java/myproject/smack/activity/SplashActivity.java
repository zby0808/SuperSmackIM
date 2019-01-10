package myproject.smack.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;

import myproject.MainXYSpHelper;

/**
 *登录窗口
 * Created by zby on 2018/11/29.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redirect();
    }

    private void redirect() {
        MainXYSpHelper helper = MainXYSpHelper.getInstance(Utils.getApp());
        String token = helper.getToken();
        if (TextUtils.isEmpty(token)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
