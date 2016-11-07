package view.adapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import view.listener.ItemClickListener;
import view.viewholder.BaseViewHolder;

/**
 * Created by lucas on 11/7/16.
 */

public abstract class BaseAdapter<D, B extends ViewDataBinding> extends RecyclerView.Adapter<BaseViewHolder<B>> implements AdapterView.OnItemClickListener, List<D> {
    private Context mContext;
    private LayoutInflater mInflater;

    public BaseAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public BaseViewHolder<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(getInflater(), viewType, parent, false);
        BaseViewHolder<B> viewHolder = new BaseViewHolder<>(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<B> holder, int position) {
        onBindViewDataBinding(holder, position);
        holder.getbinding().executePendingBindings();
    }

    protected abstract void onBindViewDataBinding(BaseViewHolder<B> holder, int position);

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @BindingAdapter(value = {"app:itemOnClick", "adapter", "vh"}, requireAll = false)
    public static void onClick(final View view, final ItemClickListener itemClick, final BaseAdapter adapter, final BaseViewHolder vh) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.itemClickListener(view, adapter, vh);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
