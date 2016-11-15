package view.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ItemViewPagerBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import view.entity.Image;

/**
 * Created by lucas on 11/8/16.
 */

public class ImagePagerAdapter extends PagerAdapter implements List<Image> {
    private ItemViewPagerBinding binding;
    private List<Image> imgs = new ArrayList<>();

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position > -1 && position < imgs.size()) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            binding = DataBindingUtil.inflate(inflater, R.layout.item_view_pager, container, false);
            binding.setData(get(position));
            binding.getRoot().setTag("pic" + position);
            container.addView(binding.getRoot());
            return binding.getRoot();
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(binding.ivPicture);
    }

    @Override
    public int getItemPosition(Object object) {
        if (imgs != null && imgs.size() == 0) {
            return POSITION_NONE;
        }
        return POSITION_UNCHANGED;
    }

    @Override
    public int size() {
        return imgs.size();
    }

    @Override
    public boolean isEmpty() {
        return imgs.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return imgs.contains(o);
    }

    @NonNull
    @Override
    public Iterator<Image> iterator() {
        return imgs.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return imgs.toArray(new Object[imgs.size()]);
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] a) {
        return imgs.toArray(a);
    }

    @Override
    public boolean add(Image image) {
        imgs.add(image);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        imgs.remove(o);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return imgs.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Image> c) {
        imgs.addAll(c);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Image> c) {
        imgs.addAll(index, c);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        imgs.removeAll(c);
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        imgs.retainAll(c);
        return true;
    }

    @Override
    public void clear() {
        imgs.clear();
    }

    @Override
    public Image get(int index) {
        return imgs.get(index);
    }

    @Override
    public Image set(int index, Image element) {
        imgs.set(index, element);
        return element;
    }

    @Override
    public void add(int index, Image element) {
        imgs.add(index, element);
    }

    @Override
    public Image remove(int index) {
        imgs.remove(index);
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return imgs.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return imgs.lastIndexOf(o);
    }

    @Override
    public ListIterator<Image> listIterator() {
        return imgs.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<Image> listIterator(int index) {
        return imgs.listIterator();
    }

    @NonNull
    @Override
    public List<Image> subList(int fromIndex, int toIndex) {
        return imgs.subList(fromIndex, toIndex);
    }

}
