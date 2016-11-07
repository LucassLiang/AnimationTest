package view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ItemPictureBinding;

import view.activity.ZoomInActivity;
import view.viewholder.BaseViewHolder;

/**
 * Created by lucas on 11/2/16.
 */

public class PictureAdapter extends ListAdapter<view.entity.Image, ItemPictureBinding> implements View.OnClickListener {

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

        holder.getbinding().getRoot().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ZoomInActivity.class);
        v.getContext().startActivity(intent);
        ((Activity) v.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
