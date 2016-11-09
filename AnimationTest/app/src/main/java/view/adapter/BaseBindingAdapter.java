package view.adapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by lucas on 11/7/16.
 */

public class BaseBindingAdapter {
    @BindingAdapter("app:url")
    public static void loadUrl(ImageView imageView, String uri) {
        Glide.with(imageView.getContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
}
