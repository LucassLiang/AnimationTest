package view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityMainBinding;

import view.adapter.PictureAdapter;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener, AppBarLayout.OnOffsetChangedListener {
    private ActivityMainBinding binding;
    private PictureAdapter mAdapter;

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
        binding.toolbarLayout.setTitle("Tree");
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
        for (int i = 0; i < 10; i++) {
            mAdapter.add(1);
        }
        mAdapter.notifyDataSetChanged();
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
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                break;
            case R.id.action_change:
//                View view = binding.getRoot();
//                float endRadius = getWindowManager().getDefaultDisplay().getHeight();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    int centerX = (view.getLeft() + view.getRight()) / 2;
//                    int centerY = (view.getBottom() + view.getBottom()) / 2;
//                    Animator animation = ViewAnimationUtils.createCircularReveal(view,
//                            centerX,
//                            centerY,
//                            0,
//                            endRadius);
//                    animation.setDuration(1000);
//                    animation.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
                intent = new Intent(MainActivity.this, ThemeActivity.class);
                startActivity(intent);
//                        }
//                    });
//                    animation.start();
//                }
                break;
        }
        return true;
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
}
