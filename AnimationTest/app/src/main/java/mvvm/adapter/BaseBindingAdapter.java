package mvvm.adapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.lucas.animationtest.R;

/**
 * Created by lucas on 11/7/16.
 */

public class BaseBindingAdapter {
    @BindingAdapter("app:url")
    public static void loadUrl(final ImageView imageView, String uri) {
        Glide.with(imageView.getContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.shape_image)
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        imageView.setClickable(false);
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        imageView.setClickable(true);
                    }
                });
    }

    @BindingAdapter("app:thumbnail")
    public static void setThumbnail(ImageView imageView, String uri) {
        Glide.with(imageView.getContext())
                .load(uri)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.shape_image)
                .into(imageView);
    }

    @BindingAdapter("app:layout_gravity")
    public static void setLayoutGravity(LinearLayout layout, boolean isAuthor) {
        layout.setGravity(isAuthor ? Gravity.LEFT : Gravity.RIGHT);
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, boolean needShow) {
        view.setVisibility(needShow ? View.VISIBLE : View.GONE);
    }
}
