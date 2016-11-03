package view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 11/2/16.
 */

public class PictureAdapter extends RecyclerView.Adapter {
    private List<Integer> pictures = new ArrayList<>();
    private ViewGroup viewGroup;
    private int layoutRes;

    public PictureAdapter(ViewGroup viewGroup, int layoutRes) {
        this.viewGroup = viewGroup;
        this.layoutRes = layoutRes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder pictureViewHolder = PictureViewHolder.getViewHolder(viewGroup, layoutRes);
        return pictureViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public void add(int position) {
        pictures.add(position);
    }

    public void remove(int position) {
        pictures.remove(position);
    }

    public void clearAll() {
        pictures.clear();
    }

    public int size() {
        return pictures.size();
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    static class PictureViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> views;
        private View mView;
        private ViewGroup mViewGroup;

        public PictureViewHolder(View view, ViewGroup viewGroup) {
            super(view);
            mView = view;
            mViewGroup = viewGroup;
            views = new SparseArray<>();
        }

        public static PictureViewHolder getViewHolder(ViewGroup viewGroup, int layoutRes) {
            View pictureView = LayoutInflater.from(viewGroup.getContext()).inflate(layoutRes, viewGroup, false);
            PictureViewHolder pictureViewHolder = new PictureViewHolder(pictureView, viewGroup);
            return pictureViewHolder;
        }

        public <T extends View> T getView(int viewId) {
            View view = getView(viewId);
            if (view == null) {
                view = mView.findViewById(viewId);
                views.put(viewId, view);
            }
            return (T) view;
        }
    }
}
