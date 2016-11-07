package view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by lucas on 11/4/16.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    private Context context;
    private View itemView;

    protected BaseViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.itemView = itemView;
        views = new SparseArray<View>();
    }

    protected <T extends View> T retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder setText(int viewId, String text) {
        TextView textView = retrieveView(viewId);
        textView.setText(text);
        return this;
    }

    public BaseViewHolder setImg(int viewId, String imgUrl) {
        ImageView imageView = retrieveView(viewId);
        Glide.with(context).load(imgUrl).into(imageView);
        return this;
    }

    public BaseViewHolder setVisibility(int viewId, boolean visible) {
        View view = retrieveView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }
}
