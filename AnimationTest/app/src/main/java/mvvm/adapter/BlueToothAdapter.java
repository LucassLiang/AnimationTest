package mvvm.adapter;

import android.content.Context;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ItemBlueToothBinding;

import mvvm.viewholder.BaseViewHolder;
import mvvm.viewmodel.BlueToothItemVModel;

/**
 * Created by lucas on 2016/11/28.
 */

public class BlueToothAdapter extends ListAdapter<BlueToothItemVModel, ItemBlueToothBinding> {
    private ItemBlueToothBinding binding;

    public BlueToothAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected void onBindViewDataBinding(BaseViewHolder<ItemBlueToothBinding> holder, int position) {
        super.onBindViewDataBinding(holder, position);
        binding = holder.getbinding();
        binding.setViewModel(get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_blue_tooth;
    }
}
