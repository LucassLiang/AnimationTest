package view.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityMainBinding;

import view.adapter.PictureAdapter;

public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener, AppBarLayout.OnOffsetChangedListener {
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
        mAdapter.notifyItemRangeInserted(0, mAdapter.size());
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
        if (item.getItemId() == R.id.action_next) {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
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
