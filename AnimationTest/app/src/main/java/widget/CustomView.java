package widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;

/**
 * Created by lucas on 2016/11/22.
 */

public class CustomView extends View {
    private Paint mPaint;
    private Path mPath;
    private int centerX;
    private int centerY;
    private static final float C = 0.551915024494f;
    private float radius = 100;
    private float mDiff = radius * C;
    private float cDistance = 0.45f * mDiff;
    private VPoint pointLeft, pointRight;
    private HPoint pointTop, pointBottom;
    private float distanceMax;

    private float currentTime;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mPath = new Path();
        pointTop = new HPoint();
        pointBottom = new HPoint();
        pointLeft = new VPoint();
        pointRight = new VPoint();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        distanceMax = getHeight() - 2 * radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        canvas.translate(getWidth() / 2, radius);
        canvas.scale(1, -1);

        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(1);
        if (0 <= currentTime && currentTime <= 0.2) {
            model1(currentTime);
        } else if (0.2 < currentTime && currentTime <= 0.5) {
            model2(currentTime);
        } else if (0.5 < currentTime && currentTime <= 0.8) {
            model3(currentTime);
        } else if (0.8 < currentTime && currentTime <= 0.9) {
            model4(currentTime);
        } else if (0.9 < currentTime && currentTime <= 1.0) {
            model5(currentTime);
        }

        //translate
        float offset = distanceMax * (currentTime - 0.2f);
        offset = offset > 0 ? offset : 0;
        pointTop.transAllY(offset);
        pointBottom.transAllY(offset);
        pointLeft.transAllY(-offset);
        pointRight.transAllY(-offset);


        mPath.moveTo(pointTop.x, pointTop.y);
        mPath.cubicTo(pointTop.right.x, pointTop.right.y, pointRight.top.x, pointRight.top.y, pointRight.x, pointRight.y);
        mPath.cubicTo(pointRight.bottom.x, pointRight.bottom.y, pointBottom.right.x, pointBottom.right.y, pointBottom.x, pointBottom.y);
        mPath.cubicTo(pointBottom.left.x, pointBottom.left.y, pointLeft.bottom.x, pointLeft.bottom.y, pointLeft.x, pointLeft.y);
        mPath.cubicTo(pointLeft.top.x, pointLeft.top.y, pointTop.left.x, pointTop.left.y, pointTop.x, pointTop.y);

        canvas.drawPath(mPath, mPaint);
    }

    private void model0() {
        pointTop.setY(radius);
        pointBottom.setY(-radius);
        pointBottom.left.x = pointTop.left.x = -mDiff;
        pointBottom.right.x = pointTop.right.x = mDiff;
        pointTop.x = pointBottom.x = 0;

        pointLeft.setX(-radius);
        pointRight.setX(radius);
        pointLeft.top.y = pointRight.top.y = mDiff;
        pointLeft.bottom.y = pointRight.bottom.y = -mDiff;
        pointLeft.y = pointRight.y = 0;
    }

    private void model1(float time) {
        model0();
        pointBottom.setY(-radius - time * 5 * radius);
        Log.i("TAG", "pointTop: " + pointTop.x + "/" + pointTop.y);
    }

    private void model2(float time) {
        model1(0.2f);
        time = (time - 0.2f) * (10f / 3);
        pointTop.adjustX(cDistance * time);
        pointBottom.adjustX(cDistance * time);
        pointLeft.transAllY(-radius / 2 * time);
        pointRight.transAllY(-radius / 2 * time);
    }

    private void model3(float time) {
        model2(0.5f);
        time = (time - 0.5f) * (10f / 3);
        pointTop.adjustX(-cDistance * time);
        pointBottom.adjustX(-cDistance * time);
        pointLeft.transAllY(-radius / 2 * time);
        pointRight.transAllY(-radius / 2 * time);

        pointTop.transAllY(radius / 2 * time);
    }

    private void model4(float time) {
        model3(0.8f);
        time = (time - 0.8f) * 10;
        pointTop.transAllY(radius / 2 * time);
    }

    private void model5(float time) {
        model4(0.9f);
        time = time - 0.9f;
        pointTop.transAllY((float) (Math.sin(Math.PI * time * 10f) * (2 / 10f * radius)));
    }

    class VPoint {
        public float x;
        public float y;
        public PointF top = new PointF();
        public PointF bottom = new PointF();

        public void setX(float x) {
            this.x = x;
            top.x = x;
            bottom.x = x;
        }

        public void adjustY(float offset) {
            top.y -= offset;
            bottom.y += offset;
        }

        public void transAllX(float offset) {
            this.x += offset;
            top.x += offset;
            bottom.x += offset;
        }

        public void transAllY(float offset) {
            this.y += offset;
            top.y += offset;
            bottom.y += offset;
        }
    }

    class HPoint {
        public float x;
        public float y;
        public PointF left = new PointF();
        public PointF right = new PointF();

        public void setY(float y) {
            this.y = y;
            left.y = y;
            right.y = y;
        }

        public void adjustX(float offset) {
            left.x += offset;
            right.x -= offset;
        }

        public void transAllY(float offset) {
            this.y -= offset;
            left.y -= offset;
            right.y -= offset;
        }

        public void transAllX(float offset) {
            this.x += offset;
            left.x += offset;
            right.x += offset;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                event.getX();
                break;
        }
        return super.onTouchEvent(event);
    }

    public class MoveAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            currentTime = interpolatedTime;
            invalidate();
        }
    }

    public void startAnimation(long duration) {
        mPath.reset();
        currentTime = 0;
        MoveAnimation animation = new MoveAnimation();
        animation.setDuration(duration);
        animation.setInterpolator(new BounceInterpolator());
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        startAnimation(animation);
    }
}
