package view.adapter;

import android.content.Context;
import android.view.View;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ItemPictureBinding;

import view.viewholder.BaseViewHolder;

/**
 * Created by lucas on 11/2/16.
 */

public class PictureAdapter extends ListAdapter<view.entity.Image, ItemPictureBinding> {
    private ItemPictureBinding binding;
    private View.OnClickListener mListener;
    private int clickPosition;

    public PictureAdapter(Context mContext, View.OnClickListener mListener) {
        super(mContext);
        this.mListener = mListener;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_picture;
    }

    @Override
    protected void onBindViewDataBinding(final BaseViewHolder<ItemPictureBinding> holder, final int position) {
        super.onBindViewDataBinding(holder, position);
        binding = holder.getbinding();
        binding.setData(get(position));

        binding.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPosition = position;
                mListener.onClick(v);
            }
        });
    }

    public int getClickPosition() {
        return clickPosition;
    }
}
