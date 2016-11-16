package util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import mvvm.view.NotificationActivity;

/**
 * Created by lucas on 11/4/16.
 */

public class AnimationUtil {

    //startActivity with reveal animation
    public static void startActivityCircleReveal(final Activity context, final ViewGroup container, final View targetView
            , final int centerX, final int centerY, final float endRadius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animation = ViewAnimationUtils.createCircularReveal(targetView,
                    centerX,
                    centerY,
                    0,
                    endRadius);
            animation.setDuration(800);
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {
                    super.onAnimationEnd(animation);

                    Intent intent = new Intent(context, NotificationActivity.class);
                    context.startActivity(intent);
                    context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    targetView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                Animator animator = ViewAnimationUtils.createCircularReveal(targetView,
                                        centerX,
                                        centerY,
                                        endRadius,
                                        0);
                                animator.setDuration(800);
                                animator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(android.animation.Animator animation) {
                                        super.onAnimationEnd(animation);
                                        container.removeView(targetView);
                                    }
                                });
                                animator.start();
                            }
                        }
                    }, 300);
                }
            });
            animation.start();
        }
    }

    //open search bar with reveal animation
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void revealReturn(final View view,
                                    final com.miguelcatalan.materialsearchview.utils.AnimationUtil.AnimationListener listener) {
        int cx = view.getWidth() - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                24, view.getResources().getDisplayMetrics());
        int cy = view.getHeight() / 2;
        int startRadius = Math.max(view.getWidth(), view.getHeight());
        int finalRadius = 0;

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, finalRadius);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                listener.onAnimationStart(view);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                listener.onAnimationCancel(view);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }
}
