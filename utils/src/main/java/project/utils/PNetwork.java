package project.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Locale;

/**
 * (判断网络情况工具类)
 * Created by zby on 2016/12/9.
 */

public class PNetwork {
    /**
     * 判断当前环境是否联网
     *
     * @param context
     * @return true表示有网
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * 判断是否为移动网络
     *
     * @param context
     * @return true表示移动网络
     */
    public static boolean isMoblieNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() ? activeNetworkInfo.getType() == 0 : false;
    }

    /**
     * 判断是否为2G网络
     *
     * @param context
     * @return true表示2G网络
     */
    public static boolean isWapNetwork(Context context) {
        if (isMoblieNetwork(context)) {
            String proxyHost = Proxy.getDefaultHost();
            if (!TextUtils.isEmpty(proxyHost)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否为Wifi环境
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        try {
            ConnectivityManager e = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netinfo = e.getActiveNetworkInfo();
            if (netinfo != null && netinfo.isAvailable() && netinfo.isConnected()) {
                String type = netinfo.getTypeName().toLowerCase(Locale.US);
                return "wifi".equals(type);
            } else {
                return false;
            }
        } catch (Exception var4) {
            return false;
        }
    }


    /**
     * 判断网络情况
     *
     * @param context
     * @return
     */
    public static String getCurrentNetType(Context context) {
        String type = null;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = "null";
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "wifi";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA
                    || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = "2g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
                    || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = "3g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
                type = "4g";
            }
        }
        return type;
    }

}
