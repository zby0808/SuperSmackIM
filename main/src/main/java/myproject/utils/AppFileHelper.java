package myproject.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;


import java.io.File;

import myproject.smack.enumclass.MessageType;

/**
 * 应用相关文件帮助类
 *
 * Created by zby on 2018/12/3.
 */
public class AppFileHelper {

    public static String getAppRoot() {

        return Utils.getApp().getExternalCacheDir().getAbsolutePath();
    }

    public static String getAppImageCacheDir() {

        return getAppRoot() + "/image";
    }

    public static String getAppDBDir() {

        return getAppRoot() + "/db";
    }

    public static String getAppCrashDir() {

        return getAppRoot() + "/crash";
    }

    public static String getAppChatDir() {

        return getAppRoot() + "/chat";
    }

    public static File getAppChatMessageDir(int type) {

        String root = getAppChatDir();
        String child = "";
        if(type == MessageType.MESSAGE_TYPE_IMAGE.value()) {
            child = "recv_image";
        } else if(type == MessageType.MESSAGE_TYPE_VOICE.value()) {
            child = "recv_voice";
        }
        File file;
        if(TextUtils.isEmpty(child)) {
            file = new File(root);
        } else {
            file = new File(root, child);
        }
        if(!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
