package mvvm.viewholder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by lucas on 11/4/16.
 */

public class BaseViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private B mbinding;

    public BaseViewHolder(B binding) {
        super(binding.getRoot());
        mbinding = binding;
    }

    public B getbinding() {
        return mbinding;
    }
}
