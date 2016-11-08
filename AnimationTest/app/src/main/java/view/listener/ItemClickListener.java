package view.listener;

import android.view.View;

import view.adapter.BaseAdapter;
import view.viewholder.BaseViewHolder;

/**
 * Created by lucas on 11/7/16.
 */

public abstract class ItemClickListener {
    public abstract ItemClickListener itemClickListener(View view, BaseAdapter adapter, BaseViewHolder viewHolder);

    public abstract ItemClickListener itemClickListener(View view, int position);
}
