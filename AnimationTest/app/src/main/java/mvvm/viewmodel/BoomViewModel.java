package mvvm.viewmodel;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableField;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.lucas.animationtest.databinding.ActivityBoomBinding;

/**
 * Created by lucas on 2017/6/29.
 */

public class BoomViewModel implements View.OnTouchListener {
    private final static int MIN_SPEED = 250;
    private final static int MIN_DISTANCE = 250;

    private Context context;
    private ActivityBoomBinding binding;

    private float xDown;//按下x轴位置
    private float xMove;//移动x轴位置

    private VelocityTracker mTracker;//用于计算手指移动速度

    public ObservableField<String> value = new ObservableField<>("");

    public BoomViewModel(Context context, ActivityBoomBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    public void onCreate() {
        binding.llyRoot.setOnTouchListener(this);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_UP:
                recyclerVelocityTracker();
                break;
            case MotionEvent.ACTION_MOVE:
                handleMoveAction(event);
                break;
            default:
                break;
        }
        return true;
    }

    private void createVelocityTracker(MotionEvent event) {
        if (mTracker == null) {
            mTracker = VelocityTracker.obtain();
        }
        mTracker.addMovement(event);
    }

    private void recyclerVelocityTracker() {
        if (mTracker == null) {
            return;
        }
        mTracker.recycle();
        mTracker = null;
    }

    private void handleMoveAction(MotionEvent event) {
        xMove = event.getRawX();
        int distance = (int) (xMove - xDown);
        int xSpeed = getVelocityX();
        if (xSpeed >= MIN_SPEED && distance >= MIN_DISTANCE) {
            ((Activity) context).finish();
        }
    }

    private int getVelocityX() {
        mTracker.computeCurrentVelocity(1000);//以一秒为单位
        return Math.abs((int) mTracker.getXVelocity());
    }
}
