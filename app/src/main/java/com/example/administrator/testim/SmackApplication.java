package com.example.administrator.testim;

import android.content.Context;
import android.view.WindowManager;

import com.example.administrator.testim.api.SuperChatApi;
import com.example.administrator.testim.http.Okhttp3Help;
import com.item.comm.BaseApplication;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import myproject.MyApplication;

/**
 * Created by zby on 2016/12/9.
 */

public class SmackApplication extends BaseApplication {
    private static SmackApplication instance = null;
    public static SuperChatApi superApi;
    public static String MAPPLICATION = "com.myproject.MyApplication";


    public SmackApplication() {
        super(true);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //网络和缓存
        superApi = createApiService(Constants.BASE_URL,
                Okhttp3Help.getOkHttpClientBuilder(),
                SuperChatApi.class);
        handleSSLHandshake();
        new MyApplication(this);
    }


    public static SmackApplication getInstance() {
        return instance;
    }

    public int getWindowWidth() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    //屏蔽证书验证
    public void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    private void modulesApplicationInit() {
        try {
            Class<?> clazz = Class.forName(MAPPLICATION);
            Object obj = clazz.newInstance();
            if (obj instanceof MyApplication) {
                ((MyApplication) obj).initMyApplication();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
