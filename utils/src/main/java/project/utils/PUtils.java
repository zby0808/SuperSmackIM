package project.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.utils.bean.DateFormatData;

/**
 * （混合类工具，目前包括吐司、显示和隐藏软键盘、时间格式化，通过uri获取文件真实路径等）
 * (吐司目前context对象有内存溢出现象，暂时未解决，建议不用,当传入application中context对象时,不会出现内存溢出现象)
 * Created by zby on 2016/12/9.
 * from topVdn
 */
public class PUtils {

    public static Toast toast;

    public PUtils() {
    }

    /**
     * 隐藏软键盘
     */
    public static void hintKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 弹出软键盘
     */
    public static void showKeyBoard(EditText editText) {
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    /**
     * 改变窗口颜色
     *
     * @param bgColor 1F完全透明  0F完全不透明
     */
    public static void darkenBackground(Activity mContext, Float bgColor) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgColor;
        mContext.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mContext.getWindow().setAttributes(lp);

    }

    /**
     * 短提示
     * 注意:Toast本身是异步,当弹出时Activity可能已经销毁,
     * 所以Context 尽量传 getApplicationContext(),防止内存泄漏
     */
    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);//设置吐司的位置
        toast.show();
    }

    /**
     * 长提示
     * 注意:Toast本身是异步,当弹出时Activity可能已经销毁,
     * 所以Context 尽量传 getApplicationContext(),防止内存泄漏
     */
    public static void showLongToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_LONG);
        } else {
            toast.setText(content);
        }
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, -250);
        toast.show();
    }

    /**
     * 判断字符串是否为IP地址
     */
    public static boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        String rex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rex);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();

        return ipAddress;
    }

    /**
     * 获取系统当前时间
     *
     * @return 格式化后的时间
     */
    public static String getCurrentTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    /**
     * 系统时间格式化
     *
     * @param timemillis SystemClock
     * @return
     */
    public static String getFormatDate(long timemillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timemillis);
        Date d = c.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(d);
    }

    //短信验证码请求专用时间格式
    public static String getSmsFormatDate(long timemillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timemillis);
        Date d = c.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(d);
    }

    /**
     * 标准时间转换成 HH-mm格式
     *
     * @param date 标准时间格式
     * @return 切割后的时间
     */
    public static String formatDate2HM(String date) {
        return date.substring(11, 16);
    }

    /**
     * 标准时间转换成 HH格式
     *
     * @param date 标准时间格式
     * @return 切割后的时间
     */
    public static String formatDate2H(String date) {
        return date.substring(11, 13);
    }

    /**
     * 获取时间间隔毫秒
     *
     * @param currentTime
     * @param oldTime
     * @return
     */
    public static long convertDifferDate(String currentTime, String oldTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = null;
        try {
            now = df.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = null;
        try {
            date = df.parse(oldTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long l = now.getTime() - date.getTime();
        return l;
    }

    /**
     * 标准时间转换成 MM-dd格式
     *
     * @param date 标准时间格式
     * @return 切割后的时间
     */
    public static String formatDate2MD(String date) {
        return date.substring(5, 10);
    }

    /**
     * 标准时间转换成 mm-ss格式
     *
     * @param date 标准时间格式
     * @return 切割后的时间
     */
    public static String formatDate2MS(String date) {
        return date.substring(3, 8);
    }

    /**
     * 标准时间转换成yyyy-MM-dd格式
     *
     * @param date 标准时间格式
     * @return 切割后的时间
     */
    public static String formatDate2YMD(String date) {
        return date.substring(0, 10);
    }

    /**
     * 标准时间转化为HH:mm:ss格式
     *
     * @param date 标准时间格式
     * @return 切割后的时间
     */
    public static String formatDate2HMS(String date) {
        return date.substring(11);
    }

    /**
     * Date转星期几
     *
     * @param dt
     * @return 字符串形式星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static JSONObject getPreviewParams(String fileName) throws JSONException {
        String[] Params = fileName.split("-");
        JSONObject JParams = new JSONObject();
        if (Params.length >= 3) {
            JParams.put("sn", Params[0]);
            JParams.put("time", Params[1]);
            JParams.put("type", Params[2]);
        }

        return JParams;
    }

    public static JSONObject getThumbParams(String fileName) throws JSONException {
        JSONObject JParams = new JSONObject();
        JParams.put("sn", fileName);
        return JParams;
    }

    public static String getBody(String[] pairKeys, String... params) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < pairKeys.length; ++i) {
            sb.append(pairKeys[i] + "=" + params[i] + "&");
        }

        return sb.toString();
    }

    /**
     * 毫秒转00:00:00
     *
     * @param time
     * @return
     */
    public static String formatLongToTimeStr(Long time) {
        int hour = 0;
        int minute = 0;
        int second = 0;

        second = time.intValue() / 1000;

        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hour) + ":" + getTwoLength(minute) + ":" + getTwoLength(second));
    }

    private static String getTwoLength(final int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

    /**
     * 获取设备IMEI序列号
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {
        TelephonyManager telManage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telManage.getDeviceId();
    }

    /**
     * 判断设备是否获取root权限
     *
     * @return
     */
    public static boolean IsRoot() {
        Process process = null;
        DataOutputStream os = null;

        boolean var3;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            return true;
        } catch (Exception var13) {
            var3 = false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }

                process.destroy();
            } catch (Exception var12) {
            }

        }

        return var3;
    }

    /**
     * 判断Wifi加密方式
     *
     * @param auth
     * @return
     */
    public static int getAuth(String auth) {
        return auth.contains("Enterprise") ? 4 : (auth.contains("EAP") ? 4 : (auth.contains("WPA2") ? 3 : (auth.contains("WPA") ? 2 : (auth.contains("WEP") ? 1 : (auth.equals("") ? 0 : 5)))));
    }


    /**
     * 安装APK
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取当前程序的版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        String version = "0";
        try {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 打开浏览器
     *
     * @param context
     * @param urlStr
     */
    public static void startBrowser(Context context, String urlStr) {
        try {
            Intent e = new Intent();
            e.setAction("android.intent.action.VIEW");
            Uri urlData = Uri.parse(urlStr);
            e.setData(urlData);
            context.startActivity(e);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    /**
     * 通过Uri得到文件真实路径
     *
     * @param context 上下文
     * @param uri     文件Uri路径
     * @return 文件的系统路径
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 对比时间转换描述时间
     *
     * @param differParm 时间间隔毫秒
     * @return 返回描述
     */
    public static DateFormatData formatDateResult(long differParm) {
        DateFormatData result = new DateFormatData();
        result.setDifferDesc("刚刚");
        long day = differParm / (24 * 60 * 60 * 1000);
        long hour = (differParm / (60 * 60 * 1000) - day * 24);
        long min = ((differParm / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (differParm / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (hour > 0) {
            result.setDifferDesc(hour + "小时前");
            result.setDiffer(hour);
            result.setDay(true);
        } else if (min > 0) {
            result.setDifferDesc(min + "分钟前");
            result.setDiffer(min);
            result.setDay(true);
        } else if (s > 0) {
            result.setDifferDesc(s + "秒钟前");
            result.setDiffer(s);
            result.setDay(true);
        }
        return result;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
