package view.util;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import view.entity.Image;

/**
 * Created by lucas on 11/10/16.
 */

public class ZoomInUtil {

    public static void initZoomInAnimation(int position, View fromView, View toView, ViewPager container) {

        Rect fromRect = new Rect();
        Point fromPoint = new Point();
        Rect toRect = new Rect();
        Point toPoint = new Point();

        fromView.getGlobalVisibleRect(fromRect, fromPoint);
        toView.getGlobalVisibleRect(toRect, toPoint);

        fromRect.offset(-toPoint.x, -toPoint.y);
        toRect.offset(-toPoint.x, -toPoint.y);

        float ratio = getZoomInRatio(fromRect, toRect);
        adjustPosition(fromRect, toRect, ratio, position);

        container.setPivotX(0);
        container.setPivotY(0);

        zoomInAnimation(container, ratio, fromRect, toRect);
    }

    private static float getZoomInRatio(Rect fromRect, Rect toRect) {
        float ratio;
        if ((float) toRect.width() / toRect.height() > (float) fromRect.width() / fromRect.height()) {
            ratio = (float) fromRect.height() / toRect.height();
        } else {
            ratio = (float) fromRect.width() / toRect.width();
        }
        return ratio;
    }

    //调整动画位置
    private static void adjustPosition(Rect fromRect, Rect toRect, float ratio, int position) {
        if ((float) toRect.width() / toRect.height() > (float) fromRect.width() / fromRect.height()) {
            float deltaWidth = getDeltaWidth(ratio, fromRect, toRect);
            fromRect.left -= deltaWidth;
            fromRect.right += deltaWidth;
            return;
        }
        float fromHeight = (float) toRect.height() * ratio;
        float deltaHeight = (fromHeight - fromRect.height()) / 2;
        fromRect.top -= deltaHeight;
        fromRect.bottom += deltaHeight;

        float fromWidth = (float) toRect.width() * ratio;
        float deltaWidth = (fromWidth - fromRect.width()) / 2;
        if ((position + 1) % 3 == 0) {
            fromRect.left -= fromWidth - fromRect.width();
        } else if ((position + 1) % 3 != 1) {
            fromRect.left -= deltaWidth;
            fromRect.right += deltaWidth;
        }
    }

    private static float getDeltaWidth(float ratio, Rect fromRect, Rect toRect) {
        float fromWidth = (int) (toRect.width() * ratio);
        return (fromWidth - fromRect.width()) / 2;
    }

    private static void zoomInAnimation(ViewPager container, float ratio, Rect fromRect, Rect toRect) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(container, View.X, fromRect.left, toRect.left))
                .with(ObjectAnimator.ofFloat(container, View.Y, fromRect.top, toRect.top))
                .with(ObjectAnimator.ofFloat(container, View.SCALE_X, ratio, 1))
                .with(ObjectAnimator.ofFloat(container, View.SCALE_Y, ratio, 1));
        set.setDuration(500);
        set.start();
    }

    public static void initZoomOutAnimation(int position, Image image, View fromView, View toView
            , ViewGroup container, int screenWidth, AnimatorListenerAdapter listener) {
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

        float ratio = getZoomOutRatio(image, toRect, screenWidth);
        adjustPosition(toRect, fromRect, ratio, position);

        zoomOutAnimation(fromView, ratio, fromRect, toRect, listener);
    }

    private static float getZoomOutRatio(Image image, Rect toRect, int screenWidth) {
        float ratio;
        float scale = (float) screenWidth / image.getImageWidth();
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
