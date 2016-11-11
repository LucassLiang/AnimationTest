package view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityMainBinding;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import view.adapter.ImagePagerAdapter;
import view.adapter.PictureAdapter;
import view.dto.ImageDTO;
import view.entity.Image;
import view.service.ApiService;
import view.transformer.ImagePageTransformer;
import view.util.AnimationUtil;
import view.util.ZoomInUtil;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
        , Toolbar.OnMenuItemClickListener, AppBarLayout.OnOffsetChangedListener, View.OnClickListener
        , ViewPager.OnPageChangeListener {
    public static final int REQUEST_CODE = 1;
    private ActivityMainBinding binding;
    private PictureAdapter mAdapter;
    private ImagePagerAdapter vpAdapter;

    private long lastTime = 0;
    private boolean isFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.actionBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.actionBarLayout.removeOnOffsetChangedListener(this);
    }

    private void initView() {
        initViewPager();
        initToolbar();
        initRecyclerView();
        binding.srlLayout.setEnabled(false);
        binding.srlLayout.setOnRefreshListener(this);
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
        binding.toolbarLayout.setTitle("Album");
        binding.toolbar.inflateMenu(R.menu.menu_item);
        binding.toolbar.setOnMenuItemClickListener(this);
    }

    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        manager.setSmoothScrollbarEnabled(true);
        manager.setAutoMeasureEnabled(true);
        binding.recyclerView.setLayoutManager(manager);

        mAdapter = new PictureAdapter(binding.getRoot().getContext(), this);
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.setHasFixedSize(true);
    }

    private void getData() {
        binding.srlLayout.setRefreshing(true);
        ApiService.getPictureService().getPicture()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ImageDTO>() {
                    @Override
                    public void onCompleted() {
                        binding.srlLayout.setRefreshing(false);
                        binding.srlLayout.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(ImageDTO imageDTO) {
                        List<Image> images = imageDTO.getImgs();
                        for (int i = 0; i < images.size() - 1; i++) {
                            mAdapter.add(images.get(i));
                            vpAdapter.add(images.get(i));
                        }
                        mAdapter.notifyDataSetChanged();
                        vpAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onRefresh() {
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.clear();
                vpAdapter.clear();
                binding.vpImgs.removeAllViews();
                vpAdapter.notifyDataSetChanged();
                getData();
            }
        }, 200);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                startAnimation();
                break;
            case R.id.action_change:
                Intent intent = new Intent(MainActivity.this, ThemeActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
        return true;
    }

    private void startAnimation() {
        int height = getWindowManager().getDefaultDisplay().getHeight();
        int width = getWindowManager().getDefaultDisplay().getWidth();
        final int endRadius = (int) Math.sqrt(height * height + width * width);

        final ViewGroup container = binding.clMain;
        final View targetView = getLayoutInflater().inflate(R.layout.activity_notification, container, false);
        container.addView(targetView, container.getWidth(), container.getHeight());
        View next = binding.toolbar.getChildAt(0);
        final int centerX = (next.getRight() + next.getLeft()) / 2;
        final int centerY = (next.getBottom() + next.getTop()) / 2;
        AnimationUtil.startActivityCircleReveal(this, container, targetView, centerX, centerY, endRadius);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        binding.srlLayout.setEnabled(verticalOffset == 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    recreate();
                    break;
            }
        }
    }

    @Override
    public void onClick(final View v) {
        int position = mAdapter.getClickPosition();
        binding.vpImgs.setCurrentItem(position, false);
        ZoomInUtil.initZoomInAnimation(position, v, binding.getRoot(), binding.vpImgs);
        pictureZoomIn();
    }

    private void pictureZoomIn() {
        backgroundAnim(true, 300);
    }

    private void backgroundAnim(final boolean needShow, long duration) {
        binding.viewBackground
                .animate()
                .alpha(needShow ? 1 : 0)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        if (needShow) {
                            binding.vpImgs.setVisibility(View.VISIBLE);
                            isFullScreen = true;
                        } else {
                            binding.actionBarLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (needShow) {
                            binding.actionBarLayout.setVisibility(View.GONE);
                        }
                    }
                })
                .start();
    }

    @Override
    public void onBackPressed() {
        if (isFullScreen) {
            outOfFullScreen();
        } else {
            if (System.currentTimeMillis() - lastTime > 1000) {
                Snackbar.make(binding.clMain, "Are you sure to quit?", Snackbar.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
            lastTime = System.currentTimeMillis();
        }
    }

    private void outOfFullScreen() {
        int currentItem = binding.vpImgs.getCurrentItem();
//        int screenWidth = getResources().getDisplayMetrics().heightPixels;
        DisplayMetrics display = getResources().getDisplayMetrics();
        View toView = mAdapter.getViews().get(currentItem);

        AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.vpImgs.setVisibility(View.GONE);
                isFullScreen = false;
            }
        };

        ZoomInUtil.initZoomOutAnimation(currentItem, mAdapter.get(currentItem), binding.vpImgs,
                toView, binding.clMain, display, listener);

        backgroundAnim(false, 300);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        GridLayoutManager manager = (GridLayoutManager) binding.recyclerView.getLayoutManager();
        manager.scrollToPosition(position);
        binding.actionBarLayout.setExpanded(position < 2, false);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
