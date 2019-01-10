package com.item.comm.util;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by Fracesuit on 2017/8/24.
 */

public class IntentUtils2 {

    /**
     * 获取App具体设置的意图
     *
     * @param packageName 包名
     * @return intent
     */
    public static Intent getAppDetailsSettingsIntent(String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        return intent;
    }
}
