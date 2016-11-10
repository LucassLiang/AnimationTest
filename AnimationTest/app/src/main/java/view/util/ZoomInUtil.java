package view.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by lucas on 11/10/16.
 */

public class ZoomInUtil {

    public static float initZoomInPosition(int position, Rect fromRect, Rect toRect) {
        float ratio;
        float toRatio = (float) toRect.width() / (float) toRect.height();
        float fromRatio = (float) fromRect.width() / (float) fromRect.height();
        if (toRatio > fromRatio) {
            ratio = (float) fromRect.height() / (float) toRect.height();
            int fromWidth = (int) (toRect.width() * ratio);
            int deltaWidth = (fromWidth - toRect.width()) / 2;
            fromRect.left -= deltaWidth;
            fromRect.right += deltaWidth;
        } else {
            ratio = (float) fromRect.width() / (float) toRect.width();
            int fromHeight = (int) (toRect.height() * ratio);
            int deltaHeight = (fromHeight - fromRect.height()) / 2;
            fromRect.top -= deltaHeight;
            fromRect.bottom += deltaHeight;

            int fromWidth = (int) (toRect.width() * ratio);
            int deltaWidth = (fromWidth - fromRect.width()) / 2;
            if ((position + 1) % 3 == 0) {
                fromRect.left -= fromWidth - fromRect.width();
            } else if ((position + 1) % 3 != 1) {
                fromRect.left -= deltaWidth;
                fromRect.right += deltaWidth;
            }
        }
        return ratio;
    }

    public static void initZoomInAnimation(View view,Rect fromRect, Rect toRect, float ratio) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, View.X, fromRect.left, toRect.left))
                .with(ObjectAnimator.ofFloat(view, View.Y, fromRect.top, toRect.top))
                .with(ObjectAnimator.ofFloat(view, View.SCALE_X, ratio, 1))
                .with(ObjectAnimator.ofFloat(view, View.SCALE_Y, ratio, 1));
        set.setDuration(500);
        set.start();
    }
}
