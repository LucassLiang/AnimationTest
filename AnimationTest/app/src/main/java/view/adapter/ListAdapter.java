package view.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import view.viewholder.BaseViewHolder;

/**
 * Created by lucas on 11/7/16.
 */

public class ListAdapter<D, B extends ViewDataBinding> extends BaseAdapter<D, B> {
    private List<D> data = new ArrayList<>();

    public ListAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected void onBindViewDataBinding(BaseViewHolder<B> holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.isEmpty() ? 0 : data.size();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (data.contains(o)) {
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public Iterator<D> iterator() {
        return data.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return data.toArray(new Object[data.size()]);
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] a) {
        return data.toArray(a);
    }

    @Override
    public boolean add(D d) {
        data.add(d);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        data.remove(o);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (data.containsAll(c)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends D> c) {
        data.addAll(c);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends D> c) {
        data.addAll(index, c);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        data.removeAll(c);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        data.retainAll(c);
        return true;
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public D get(int index) {
        return data.get(index);
    }

    @Override
    public D set(int index, D element) {
        return data.set(index, element);
    }

    @Override
    public void add(int index, D element) {
        data.add(index, element);
    }

    @Override
    public D remove(int index) {
        return data.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return data.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }

    @Override
    public ListIterator<D> listIterator() {
        return data.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<D> listIterator(int index) {
        return data.listIterator(index);
    }

    @NonNull
    @Override
    public List<D> subList(int fromIndex, int toIndex) {
        return data.subList(fromIndex, toIndex);
    }
}
