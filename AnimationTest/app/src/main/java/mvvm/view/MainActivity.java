package mvvm.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityMainBinding;

import mvvm.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new MainViewModel(this, binding);
        binding.setViewModel(viewModel);
        viewModel.onCreate();
    }

    @Override
    public void onBackPressed() {
        viewModel.onBackPressed();
    }
}
