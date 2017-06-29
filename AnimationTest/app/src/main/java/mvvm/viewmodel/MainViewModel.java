package mvvm.viewmodel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityMainBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import data.dto.ImageDTO;
import data.service.ApiService;
import mvvm.adapter.ImagePagerAdapter;
import mvvm.adapter.PictureAdapter;
import mvvm.model.Image;
import mvvm.transformer.ImagePageTransformer;
import mvvm.view.BlueToothActivity;
import mvvm.view.BoomActivity;
import mvvm.view.CustomActivity;
import mvvm.view.LoginActivity;
import mvvm.view.MainActivity;
import mvvm.view.MapActivity;
import mvvm.view.QRActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import util.AnimationUtil;
import util.ZoomInUtil;

/**
 * Created by lucas on 16/11/2016.
 */

public class MainViewModel implements Toolbar.OnMenuItemClickListener, ViewPager.OnPageChangeListener,
        View.OnClickListener, com.miguelcatalan.materialsearchview.utils.AnimationUtil.AnimationListener {
    private PictureAdapter mAdapter;
    private ImagePagerAdapter vpAdapter;
    private ActivityMainBinding binding;
    private Activity context;

    private long lastTime = 0;
    private boolean isFullScreen = false;
    private boolean isAnimating = false;

    public MainViewModel(Activity context, ActivityMainBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    public void onCreate() {
        initView();
        getData("风景", false);
    }

    private void initView() {
        initViewPager();
        initToolbar();
        initSearchBar();
        initRecyclerView();
        binding.srlLayout.setEnabled(false);
    }

    private void initViewPager() {
        vpAdapter = new ImagePagerAdapter();
        binding.vpImgs.setVisibility(View.GONE);
        binding.vpImgs.setAdapter(vpAdapter);
        binding.vpImgs.setOffscreenPageLimit(3);

        //init pager change's animator in ViewPager
        ImagePageTransformer transformer = new ImagePageTransformer();
        binding.vpImgs.setPageTransformer(true, transformer);

        binding.vpImgs.addOnPageChangeListener(this);
    }

    private void initToolbar() {
        binding.toolbar.inflateMenu(R.menu.menu_item);
        binding.toolbar.setOnMenuItemClickListener(this);
    }

    private void initSearchBar() {
        binding.svSearch.findViewById(R.id.action_up_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearchBar();
            }
        });

        binding.svSearch.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getData(query, true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);
        manager.setSmoothScrollbarEnabled(true);
        manager.setAutoMeasureEnabled(true);
        binding.recyclerView.setLayoutManager(manager);

        mAdapter = new PictureAdapter(binding.getRoot().getContext(), this);
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.setHasFixedSize(true);
    }

    private void getData(String tag, final boolean needRefresh) {
        binding.srlLayout.setRefreshing(true);
        ApiService.getPictureService().getPicture(tag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        binding.srlLayout.setRefreshing(false);
                    }
                })
                .doOnNext(new Action1<ImageDTO>() {
                    @Override
                    public void call(ImageDTO imageDTO) {
                        handleData(imageDTO, needRefresh);
                    }
                })
                .subscribe(Actions.empty(), new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("TAG", "onError: " + throwable.toString());
                    }
                });
    }

    private void handleData(ImageDTO imageDTO, boolean needRefresh) {
        if (needRefresh) {
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            binding.vpImgs.removeAllViews();
            vpAdapter.clear();
            vpAdapter.notifyDataSetChanged();
        }
        List<Image> images = imageDTO.getImgs();
        for (int i = 0; i < images.size() - 1; i++) {
            mAdapter.add(images.get(i));
            vpAdapter.add(images.get(i));
        }
        mAdapter.notifyDataSetChanged();
        vpAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                startAnimation(CustomActivity.class, R.layout.activity_custom);
                break;
            case R.id.action_change:
                changeNightMode(item);
                break;
            case R.id.action_search:
                if (!binding.svSearch.isSearchOpen()) {
                    binding.svSearch.showSearch(true);
                }
                break;
            case R.id.action_chat:
                startAnimation(LoginActivity.class, R.layout.activity_login);
                break;
            case R.id.action_blue_tooth:
                startAnimation(BlueToothActivity.class, R.layout.activity_blue_tooth);
                break;
            case R.id.action_map:
                if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS) {
                    Intent intent = new Intent(context, MapActivity.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Google play service not found,please install Google play service.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_boom:
                context.startActivity(new Intent(context, BoomActivity.class));
                break;
            case R.id.action_qr:
                context.startActivity(new Intent(context, QRActivity.class));
                break;
        }
        return true;
    }

    private void startAnimation(Class<?> targetActivity, int layoutRes) {
        int height = context.getWindowManager().getDefaultDisplay().getHeight();
        int width = context.getWindowManager().getDefaultDisplay().getWidth();
        final int endRadius = (int) Math.sqrt(height * height + width * width);

        final ViewGroup container = binding.clMain;
        final View targetView = context.getLayoutInflater().inflate(layoutRes, container, false);
        container.addView(targetView, container.getWidth(), container.getHeight());
        View next = binding.toolbar.getChildAt(0);
        final int centerX = (next.getRight() + next.getLeft()) / 2;
        final int centerY = (next.getBottom() + next.getTop()) / 2;
        AnimationUtil.startActivityCircleReveal(context, targetActivity, container, targetView, centerX, centerY, endRadius);
    }

    //change night mode
    public void changeNightMode(MenuItem item) {
        boolean isNight = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        context.finish();
    }

    @Override
    public void onClick(final View v) {
        closeSearchBar();

        int position = mAdapter.getClickPosition();
        binding.vpImgs.setCurrentItem(position, false);
        Image currentImage = mAdapter.get(position);
        DisplayMetrics display = context.getResources().getDisplayMetrics();

        AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                binding.viewBackground.setVisibility(View.VISIBLE);
                binding.actionBarLayout.setVisibility(View.GONE);
                binding.vpImgs.setVisibility(View.VISIBLE);
                isFullScreen = true;
            }
        };

        ZoomInUtil.initZoomInAnimation(v, binding.clMain, binding.vpImgs, currentImage, display, listener);
    }

    private void closeSearchBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimationUtil.revealReturn(binding.svSearch, this);
        } else {
            binding.svSearch.closeSearch();
        }
    }

    private void outOfFullScreen() {
        int currentItem = binding.vpImgs.getCurrentItem();
        PhotoView current = getPhotoView(currentItem);
        resetScale(current);
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        View toView = mAdapter.getViews().get(currentItem);

        AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                binding.actionBarLayout.setVisibility(View.VISIBLE);
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.viewBackground.setVisibility(View.GONE);
                binding.vpImgs.setVisibility(View.GONE);
                isFullScreen = false;
                isAnimating = false;
            }
        };

        ZoomInUtil.initZoomOutAnimation(mAdapter.get(currentItem), binding.vpImgs, toView,
                binding.clMain, display, listener);

    }

    private PhotoView getPhotoView(int position) {
        View currentView = binding.vpImgs.findViewWithTag("pic" + position);
        if (currentView == null) {
            return null;
        }
        PhotoView photoView = (PhotoView) currentView.findViewById(R.id.iv_picture);
        return photoView;
    }

    private void resetScale(final PhotoView photoView) {
        if (photoView != null) {
            if (photoView.getScale() != photoView.getMinimumScale()) {
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        photoView.setScale(photoView.getMinimumScale(), true);
                    }
                }, 100);
            }
        }
    }

    @Override
    public boolean onAnimationStart(View view) {
        return false;
    }

    @Override
    public boolean onAnimationEnd(View view) {
        binding.svSearch.closeSearch();
        return true;
    }

    @Override
    public boolean onAnimationCancel(View view) {
        return false;
    }

    public void onBackPressed() {
        if (isAnimating) {
            return;
        }
        if (binding.svSearch.isSearchOpen()) {
            closeSearchBar();
            return;
        }
        if (isFullScreen) {
            outOfFullScreen();
        } else {
            if (System.currentTimeMillis() - lastTime > 1000) {
                Snackbar.make(binding.clMain, "Are you sure to quit?", Snackbar.LENGTH_SHORT).show();
            } else {
                context.finish();
            }
            lastTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
        getPhotoView(position).setOnScaleChangeListener(new PhotoViewAttacher.OnScaleChangeListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                if (scaleFactor > getPhotoView(position).getMinimumScale()) {
                    binding.vpImgs.setLockPage(true);
                } else {
                    binding.vpImgs.setLockPage(false);
                }
            }
        });
    }

    @Override
    public void onPageSelected(int position) {
        GridLayoutManager manager = (GridLayoutManager) binding.recyclerView.getLayoutManager();
        manager.scrollToPosition(position);
        if (position > 6) {
            binding.actionBarLayout.setExpanded(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
