package view.adapter;

import android.content.Context;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ItemPictureBinding;

import view.viewholder.BaseViewHolder;

/**
 * Created by lucas on 11/2/16.
 */

public class PictureAdapter extends ListAdapter<view.entity.Image, ItemPictureBinding> {

    public PictureAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_picture;
    }

    @Override
    protected void onBindViewDataBinding(BaseViewHolder<ItemPictureBinding> holder, int position) {
        super.onBindViewDataBinding(holder, position);
        holder.getbinding().setData(get(position));
    }
}
