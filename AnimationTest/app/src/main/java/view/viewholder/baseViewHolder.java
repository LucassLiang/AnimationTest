package view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by lucas on 11/4/16.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    private Context context;
    private View itemView;

    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        this.itemView = itemView;
        views = new SparseArray<View>();
    }
}
