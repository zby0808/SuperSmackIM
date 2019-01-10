package myproject.http;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;

import java.io.IOException;

import myproject.MainXYSpHelper;
import myproject.smack.activity.LoginActivity;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 重新登录拦截器。
 * Created by bangyong.zhang on 2018/6/1.
 */

public class ReLoginInterceptor implements Interceptor {

    Context context;

    public ReLoginInterceptor(Context context) {
        this.context = context;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        //拦截修改密码接口，因为密码修改错误也会返回401问题，（该块暂时用登录代替，后改）
        String url = response.request().url().url().toString();
        boolean isCheckedPwd = url.contains("login");
        if (response.code() == 401) {
            if (!isCheckedPwd) {
                gotoLoginPage();
            }
        }
        return response;
    }

    private void gotoLoginPage() {
        MainXYSpHelper helper = MainXYSpHelper.getInstance(Utils.getApp());
        helper.clearToken();
        Log.d("DEBUG", "跳转到登录");
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("code", 401);
        context.startActivity(intent);
        ActivityUtils.finishAllActivities();
    }
}
