package mvvm.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityCustomBinding;

import mvvm.viewmodel.CustomViewModel;

/**
 * Created by lucas on 10/31/16.
 */

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Custom View");
        ActivityCustomBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_custom);
        CustomViewModel viewModel = new CustomViewModel(this, binding);
        binding.setViewModel(viewModel);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
