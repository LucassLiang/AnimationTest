package mvvm.adapter;

import android.content.Context;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ItemChatBinding;

import mvvm.viewholder.BaseViewHolder;
import mvvm.viewmodel.ChatItemViewModel;

/**
 * Created by lucas on 16/11/2016.
 */

public class ChatAdapter extends ListAdapter<ChatItemViewModel, ItemChatBinding> {
    public final static long TIMEINTERVAL = 1000 * 60 * 10;
    private ItemChatBinding binding;

    public ChatAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected void onBindViewDataBinding(BaseViewHolder<ItemChatBinding> holder, int position) {
        super.onBindViewDataBinding(holder, position);
        binding = holder.getbinding();
        binding.setViewModel(get(position));
        binding.setShowTime(showTime(position));
    }

    private boolean showTime(int index) {
        if (index == 0) {
            return true;
        }
        long lastTime = get(index - 1).getMessage().getTimestamp();
        long currentTime = get(index).getMessage().getTimestamp();
        return currentTime - lastTime > TIMEINTERVAL;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_chat;
    }
}
