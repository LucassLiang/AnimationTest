package view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityMainBinding;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import view.adapter.PictureAdapter;
import view.dto.ImageDTO;
import view.service.ApiService;
import view.util.AnimationUtil;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener, AppBarLayout.OnOffsetChangedListener {
    public static final int REQUEST_CODE = 1;
    private ActivityMainBinding binding;
    private PictureAdapter mAdapter;
    private long lastTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.actionBarLayout.addOnOffsetChangedListener(this);
    }

    private void initView() {
        initToolbar();
        initRecyclerView();
        binding.srlLayout.setOnRefreshListener(this);
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

        mAdapter = new PictureAdapter(binding.recyclerView, R.layout.item_picture);
        binding.recyclerView.setAdapter(mAdapter);
        getData();
        binding.recyclerView.setHasFixedSize(true);
    }

    private void getData() {
        ApiService.getPictureService().getPicture()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ImageDTO>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(ImageDTO imageDTO) {
                        mAdapter.addAll(imageDTO.getImgs());
                        mAdapter.notifyDataSetChanged();
                        Log.e("TAG", "onNext: " + mAdapter.getData().toString());
                    }
                });
    }

    @Override
    public void onRefresh() {
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.clearAll();
                getData();
                binding.srlLayout.setRefreshing(false);
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
    protected void onPause() {
        super.onPause();
        binding.actionBarLayout.removeOnOffsetChangedListener(this);
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
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTime > 1000) {
            Snackbar.make(binding.clMain, "Are you sure to quit?", Snackbar.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
        lastTime = System.currentTimeMillis();
    }
}
