package util;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import mvvm.model.Image;

/**
 * Created by lucas on 11/10/16.
 */

public class ZoomInUtil {

    public static void initZoomInAnimation(View fromView, View toView, ViewPager container,
                                           Image currentImage, DisplayMetrics display, AnimatorListenerAdapter listener) {

        Rect fromRect = new Rect();
        Point fromPoint = new Point();
        Rect toRect = new Rect();
        Point toPoint = new Point();

        fromView.getGlobalVisibleRect(fromRect, fromPoint);
        toView.getGlobalVisibleRect(toRect, toPoint);

        fromRect.offset(-toPoint.x, -toPoint.y);
        toRect.offset(-toPoint.x, -toPoint.y);

        float ratio = getZoomInRatio(fromRect, currentImage, display);
        adjustPosition(fromRect, toRect, ratio);

        container.setPivotX(0);
        container.setPivotY(0);

        zoomInAnimation(container, ratio, fromRect, toRect, listener);
    }

    private static float getZoomInRatio(Rect fromRect, Image image, DisplayMetrics display) {
        float ratio;
        float scale = (float) display.widthPixels / image.getImageWidth();
        if (image.getImageWidth() > image.getImageHeight()) {
            ratio = fromRect.height() / (image.getImageHeight() * scale);
            if (image.getImageWidth() * ratio * scale < fromRect.width()) {
                ratio = fromRect.width() / (image.getImageHeight() * scale);
            }
        } else {
            ratio = fromRect.width() / (image.getImageWidth() * scale);
            if (image.getImageHeight() * ratio * scale < fromRect.height()) {
                ratio = fromRect.height() / (image.getImageHeight() * scale);
            }
        }
        return ratio;
    }

    //调整动画位置
    private static void adjustPosition(Rect fromRect, Rect toRect, float ratio) {
        if ((float) toRect.width() / toRect.height() > (float) fromRect.width() / fromRect.height()) {
            float deltaWidth = getDeltaWidth(ratio, fromRect, toRect);
            fromRect.left -= deltaWidth;
            fromRect.right += deltaWidth;
            return;
        }
        //adjust vertical position
        float fromHeight = (float) toRect.height() * ratio;
        float deltaHeight = (fromHeight - fromRect.height()) / 2;
        fromRect.top -= deltaHeight;
        fromRect.bottom += deltaHeight;

        //adjust horizontal position
        float fromWidth = (float) toRect.width() * ratio;
        float deltaWidth = (fromWidth - fromRect.width()) / 2;
        fromRect.left -= deltaWidth;
        fromRect.right += deltaWidth;
    }

    private static float getDeltaWidth(float ratio, Rect fromRect, Rect toRect) {
        float fromWidth = (int) (toRect.width() * ratio);
        return (fromWidth - fromRect.width()) / 2;
    }

    private static void zoomInAnimation(ViewPager container, float ratio, Rect fromRect, Rect toRect,
                                        AnimatorListenerAdapter listener) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(container, View.X, fromRect.left, toRect.left))
                .with(ObjectAnimator.ofFloat(container, View.Y, fromRect.top, toRect.top))
                .with(ObjectAnimator.ofFloat(container, View.SCALE_X, ratio, 1))
                .with(ObjectAnimator.ofFloat(container, View.SCALE_Y, ratio, 1));
        set.setDuration(500);
        set.addListener(listener);
        set.start();
    }

    public static void initZoomOutAnimation(Image image, View fromView, View toView
            , ViewGroup container, DisplayMetrics display, AnimatorListenerAdapter listener) {
        Rect fromRect = new Rect();
        Point fromPoint = new Point();
        Rect toRect = new Rect();
        Point toPoint = new Point();
        toView.getGlobalVisibleRect(toRect, toPoint);
        container.getGlobalVisibleRect(fromRect, fromPoint);

        toRect.offset(-fromPoint.x, -fromPoint.y);
        fromRect.offset(-fromPoint.x, -fromPoint.y);

        container.setPivotX(0);
        container.setPivotY(0);

        float ratio = getZoomOutRatio(image, toRect, display);
        adjustPosition(toRect, fromRect, ratio);

        zoomOutAnimation(fromView, ratio, fromRect, toRect, listener);
    }

    private static float getZoomOutRatio(Image image, Rect toRect, DisplayMetrics display) {
        float ratio;
        float scale = (float) display.widthPixels / image.getImageWidth();
        if (image.getImageWidth() > image.getImageHeight()) {
            ratio = toRect.height() / (scale * image.getImageHeight());
            if (ratio * scale * image.getImageWidth() < toRect.width()) {
                ratio = toRect.width() / (scale * image.getImageWidth());
            }
            return ratio;
        }
        ratio = toRect.width() / (scale * image.getImageWidth());
        if (ratio * scale * image.getImageHeight() < toRect.height()) {
            ratio = toRect.height() / (scale * image.getImageHeight());
        }
        return ratio;
    }

    private static void zoomOutAnimation(View view, float ratio, Rect fromRect, Rect toRect, AnimatorListenerAdapter listener) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, View.X, fromRect.left, toRect.left))
                .with(ObjectAnimator.ofFloat(view, View.Y, fromRect.top, toRect.top))
                .with(ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, ratio))
                .with(ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, ratio));
        set.setDuration(500);
        set.addListener(listener);
        set.start();
    }
}
