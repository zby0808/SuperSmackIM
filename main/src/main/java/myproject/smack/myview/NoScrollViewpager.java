package myproject.smack.myview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 禁止滑动的viewPager
 * Created by zby on 2016/12/21.
 */

public class NoScrollViewpager extends ViewPager {
    private boolean isScroll = true;

    public NoScrollViewpager(Context context) {
        super(context);

    }

    public NoScrollViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isScroll) {
            return true;
        }else {
            return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!isScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }


    public boolean isScrollble() {
        return isScroll;
    }

    public void setScrollble(boolean isScroll) {
        this.isScroll = isScroll;
    }
}
