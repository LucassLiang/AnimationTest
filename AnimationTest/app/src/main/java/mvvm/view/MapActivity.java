package mvvm.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityMapBinding;

import mvvm.viewmodel.MapVModel;

/**
 * Created by lucas on 2016/11/28.
 */

public class MapActivity extends FragmentActivity {
    private MapVModel mapVModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        mapVModel = new MapVModel(this, binding);
        binding.setViewModel(mapVModel);
        mapVModel.onCreate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mapVModel.onPermissionResult(requestCode, permissions, grantResults);
    }
}
