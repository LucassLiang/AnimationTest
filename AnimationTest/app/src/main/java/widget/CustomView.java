package widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
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

    public enum DIRECTION {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    private DIRECTION direction = DIRECTION.DOWN;

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
        distanceMax = getHeight() / 2 - 2 * radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.scale(1, -1);

        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(1);
        if (0 <= currentTime && currentTime <= 0.2) {
            model1(currentTime, direction);
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
        switch (direction) {
            case LEFT:
                pointTop.transAllX(-offset);
                pointBottom.transAllX(-offset);
                pointLeft.transAllX(-offset);
                pointRight.transAllX(-offset);
                break;
            case RIGHT:
                pointTop.transAllX(offset);
                pointBottom.transAllX(offset);
                pointLeft.transAllX(offset);
                pointRight.transAllX(offset);
                break;
            case UP:
                pointTop.transAllY(-offset);
                pointBottom.transAllY(-offset);
                pointLeft.transAllY(offset);
                pointRight.transAllY(offset);
                break;
            case DOWN:
                pointTop.transAllY(offset);
                pointBottom.transAllY(offset);
                pointLeft.transAllY(-offset);
                pointRight.transAllY(-offset);
                break;
        }

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

    private void model1(float time, DIRECTION direction) {
        model0();
        switch (direction) {
            case LEFT:
                moveLeft1(time);
                break;
            case RIGHT:
                moveRight1(time);
                break;
            case UP:
                moveUp1(time);
                break;
            case DOWN:
                moveDown1(time);
                break;
        }
    }

    private void moveLeft1(float time) {
        pointLeft.setX(-radius - time * 5 * radius);
    }

    private void moveRight1(float time) {
        pointRight.setX(radius + time * 5 * radius);
    }

    private void moveUp1(float time) {
        pointTop.setY(radius + time * 5 * radius);
    }

    private void moveDown1(float time) {
        pointBottom.setY(-radius - time * 5 * radius);
    }

    private void model2(float time) {
        model1(0.2f, direction);
        time = (time - 0.2f) * (10f / 3);
        switch (direction) {
            case LEFT:
                moveLeft2(time);
                break;
            case RIGHT:
                moveRight2(time);
                break;
            case UP:
                moveUp2(time);
                break;
            case DOWN:
                moveDown2(time);
                break;
        }
    }

    private void moveLeft2(float time) {
        pointLeft.adjustY(-cDistance * time);
        pointRight.adjustY(-cDistance * time);
        pointTop.transAllX(-radius / 2 * time);
        pointBottom.transAllX(-radius / 2 * time);
    }

    private void moveRight2(float time) {
        pointLeft.adjustY(-cDistance * time);
        pointRight.adjustY(-cDistance * time);
        pointTop.transAllX(radius / 2 * time);
        pointBottom.transAllX(radius / 2 * time);
    }

    private void moveUp2(float time) {
        pointTop.adjustX(-cDistance * time);
        pointBottom.adjustX(-cDistance * time);
        pointLeft.transAllY(radius / 2 * time);
        pointRight.transAllY(radius / 2 * time);
    }

    private void moveDown2(float time) {
        pointTop.adjustX(-cDistance * time);
        pointBottom.adjustX(-cDistance * time);
        pointLeft.transAllY(-radius / 2 * time);
        pointRight.transAllY(-radius / 2 * time);
    }

    private void model3(float time) {
        model2(0.5f);
        time = (time - 0.5f) * (10f / 3);
        switch (direction) {
            case LEFT:
                moveLeft3(time);
                break;
            case RIGHT:
                moveRight3(time);
                break;
            case UP:
                moveUp3(time);
                break;
            case DOWN:
                moveDown3(time);
                break;
        }
    }

    private void moveLeft3(float time) {
        pointLeft.adjustY(cDistance * time);
        pointRight.adjustY(cDistance * time);
        pointTop.transAllX(-radius / 2 * time);
        pointBottom.transAllX(-radius / 2 * time);

        pointRight.transAllX(-radius / 2 * time);
    }

    private void moveRight3(float time) {
        pointLeft.adjustY(cDistance * time);
        pointRight.adjustY(cDistance * time);
        pointTop.transAllX(radius / 2 * time);
        pointBottom.transAllX(radius / 2 * time);

        pointLeft.transAllX(radius / 2 * time);
    }

    private void moveUp3(float time) {
        pointTop.adjustX(cDistance * time);
        pointBottom.adjustX(cDistance * time);
        pointLeft.transAllY(radius / 2 * time);
        pointRight.transAllY(radius / 2 * time);

        pointBottom.transAllY(-radius / 2 * time);
    }

    private void moveDown3(float time) {
        pointTop.adjustX(cDistance * time);
        pointBottom.adjustX(cDistance * time);
        pointLeft.transAllY(-radius / 2 * time);
        pointRight.transAllY(-radius / 2 * time);

        pointTop.transAllY(radius / 2 * time);
    }

    private void model4(float time) {
        model3(0.8f);
        time = (time - 0.8f) * 10;
        switch (direction) {
            case LEFT:
                moveLeft4(time);
                break;
            case RIGHT:
                moveRight4(time);
                break;
            case UP:
                moveUp4(time);
                break;
            case DOWN:
                moveDown4(time);
                break;
        }
    }

    private void moveLeft4(float time) {
        pointRight.transAllX(-radius / 2 * time);
    }

    private void moveRight4(float time) {
        pointLeft.transAllX(radius / 2 * time);
    }

    private void moveUp4(float time) {
        pointBottom.transAllY(-radius / 2 * time);
    }

    private void moveDown4(float time) {
        pointTop.transAllY(radius / 2 * time);
    }

    private void model5(float time) {
        model4(0.9f);
        time = time - 0.9f;
        switch (direction) {
            case LEFT:
                moveLeft5(time);
                break;
            case RIGHT:
                moveRight5(time);
                break;
            case UP:
                moveUp5(time);
                break;
            case DOWN:
                moveDown5(time);
                break;
        }
    }

    private void moveLeft5(float time) {
        pointRight.transAllX((float) (Math.sin(Math.PI * time * 10f) * (2 / 10f * radius)));
    }

    private void moveRight5(float time) {
        pointLeft.transAllX((float) (-Math.sin(Math.PI * time * 10f) * (2 / 10f * radius)));
    }

    private void moveUp5(float time) {
        pointBottom.transAllY((float) (Math.sin(Math.PI * time * 10f) * (2 / 10f * radius)));
    }

    private void moveDown5(float time) {
        pointTop.transAllY((float) (-Math.sin(Math.PI * time * 10f) * (2 / 10f * radius)));
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

        public void adjustY(float offset) {
            left.y += offset;
            right.y -= offset;
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
                if (Math.abs(event.getX() - centerX) > Math.abs(event.getY() - centerY)) {
                    if (event.getX() < centerX) {
                        direction = DIRECTION.LEFT;
                    } else if (event.getX() > centerX) {
                        direction = DIRECTION.RIGHT;
                    }
                } else {
                    if (event.getY() > centerY) {
                        direction = DIRECTION.DOWN;
                        startAnimation(1000);
                    } else if (event.getY() < centerY) {
                        direction = DIRECTION.UP;
                        startAnimation(1000);
                    }
                }

                startAnimation(1000);
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
