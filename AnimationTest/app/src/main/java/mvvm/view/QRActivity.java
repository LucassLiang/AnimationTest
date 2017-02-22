package mvvm.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityQrBinding;

import mvvm.viewmodel.QRViewModel;

/**
 * Created by lucas on 2017/2/21.
 */

public class QRActivity extends AppCompatActivity {
    private QRViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityQrBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_qr);
        viewModel = new QRViewModel(this, binding);
        binding.setViewModel(viewModel);
        viewModel.onCreate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        viewModel.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        viewModel.onActivityResult(requestCode, resultCode, data);
    }
}
