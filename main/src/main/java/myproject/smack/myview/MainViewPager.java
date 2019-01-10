package myproject.smack.myview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import project.utils.PLog;

/**
 * 主界面viewpager
 * Created by zby on 2016/12/21.
 */

public class MainViewPager extends ViewPager {
    private float x_tmp1, x_tmp2, y_tmp1, y_tmp2;
    private boolean isInterception = true;

    public MainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainViewPager(Context context) {
        super(context);
    }

    /**
     * 事件分发, 请求父控件及祖宗控件是否拦截事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (getCurrentItem() != 0) {
            getParent().requestDisallowInterceptTouchEvent(true);// 用getParent去请求,
            // 不拦截
        } else {// 如果是第一个页面,需要显示侧边栏, 请求父控件拦截
            //获取当前坐标
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x_tmp1 = x;
                    y_tmp1 = y;
                    break;
                case MotionEvent.ACTION_UP:
                    x_tmp2 = x;
                    y_tmp2 = y;
                    PLog.i("滑动参值 x1=" + x_tmp1 + "; x2=" + x_tmp2);
                    if (x_tmp1 != 0 && y_tmp1 != 0) {
                        if (x_tmp1 - x_tmp2 > 8) {
                            PLog.i("向左滑动");
                            isInterception = false;

                        }
                        if (x_tmp2 - x_tmp1 > 8) {
                            PLog.i("向右滑动");
                            isInterception = true;
                        }
                    }
                    break;
            }
            getParent().requestDisallowInterceptTouchEvent(false);// 拦截
        }
        return super.dispatchTouchEvent(event);
    }

}
