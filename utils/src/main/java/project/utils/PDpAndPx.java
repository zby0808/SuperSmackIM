package project.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * dp和px 之间相互转换
 * Created by zby on 2016/12/9.
 */

public class PDpAndPx {
    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dip2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param px      像素
     * @return
     */
    public static float px2dip(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        float dp = px / density;
        return dp;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidthPixels(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
}
