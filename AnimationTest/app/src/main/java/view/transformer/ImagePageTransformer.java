package view.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by lucas on 11/8/16.
 */

public class ImagePageTransformer implements ViewPager.PageTransformer {
    private final static float SCALE_MIN = 0.1f;

    @Override
    public void transformPage(View page, float position) {
        if (position < -1) {
            page.setAlpha(0.5f);
        } else if (position <= 0) {
            page.setAlpha(1);
            page.setTranslationX(0);
            page.setScaleX(1);
            page.setScaleY(1);
        } else if (position <= 1) {
            page.setAlpha(1 - position);
            page.setTranslationX(page.getWidth() * -position);
            float scaleFactor = SCALE_MIN + (1 - SCALE_MIN) * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        } else {
            page.setAlpha(0f);
        }
    }
}
