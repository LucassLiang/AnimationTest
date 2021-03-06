package widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lucas on 14/11/2016.
 */

public class ViewPagerFixed extends ViewPager {
    private boolean lockPage = false;

    public ViewPagerFixed(Context context) {
        super(context);
    }

    public ViewPagerFixed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!lockPage) {
            try {
                return super.onTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!lockPage) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public boolean isLockPage() {
        return lockPage;
    }

    public void setLockPage(boolean lockPage) {
        this.lockPage = lockPage;
    }
}
