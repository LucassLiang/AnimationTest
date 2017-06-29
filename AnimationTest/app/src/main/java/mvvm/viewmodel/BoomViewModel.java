package mvvm.viewmodel;

import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.ObservableField;
import android.view.animation.LinearInterpolator;

/**
 * Created by lucas on 2017/6/29.
 */

public class BoomViewModel {
    private Context context;
    public ObservableField<String> value = new ObservableField<>("");

    public BoomViewModel(Context context) {
        this.context = context;
    }

    public void onCreate() {
        initAnimation();
    }

    private void initAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 123456.00f);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float temp = (float) animation.getAnimatedValue();
                value.set(String.format("About %.2f", temp));
            }
        });
        valueAnimator.start();
    }
}
