package widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lucas on 2016/11/24.
 */

public class LoadingView extends View {
    private Path mPath;
    private Paint mPaint;
    private float centerX;
    private float centerY;
    private PathMeasure mPathMeasure;

    private float mAnimationValue;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mListener;

    private Handler mHandler;

    private ValueAnimator searchAnimator;

    private boolean isOver = true;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initPaint();
        initPath();
        initListener();
        initHandler();
        initAnimator();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.CYAN);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private void initPath() {
        mPath = new Path();
        mPathMeasure = new PathMeasure();
        RectF rectf = new RectF(-30, -30, 30, 30);
        mPath.addArc(rectf, -90, 359.9f);
    }

    private void initListener() {
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimationValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        };

        mListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (!isOver) {
                    searchAnimator.start();
                }
            }
        };
    }

    private void initAnimator() {
        searchAnimator = new ValueAnimator().ofFloat(0, 1).setDuration(2000);
        searchAnimator.addListener(mListener);
        searchAnimator.addUpdateListener(mUpdateListener);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX, centerY);

        mPathMeasure.setPath(mPath, false);
        Path dst = new Path();
        float finish = mPathMeasure.getLength() * mAnimationValue;
        float start = finish - (1 - Math.abs(mAnimationValue)) * 100f;
        mPathMeasure.getSegment(start, finish, dst, true);
        canvas.drawPath(dst, mPaint);
    }

    public void startAnimator() {
        setOver(false);
        searchAnimator.start();
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public boolean isOver() {
        return isOver;
    }
}
