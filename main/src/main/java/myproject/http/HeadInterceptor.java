package myproject.http;

import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;

import java.io.IOException;

import myproject.Constants;
import myproject.MainXYSpHelper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder();

        MainXYSpHelper helper = MainXYSpHelper.getInstance(Utils.getApp());

        if (helper != null) {
            if (!TextUtils.isEmpty(helper.getToken())) {
                requestBuilder.addHeader(Constants.TOKEN_KEY, helper.getToken());
            }

        }

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
