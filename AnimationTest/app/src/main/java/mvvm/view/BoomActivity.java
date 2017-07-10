package mvvm.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityBoomBinding;

import mvvm.viewmodel.BoomViewModel;

/**
 * Created by lucas on 2017/6/29.
 */

public class BoomActivity extends AppCompatActivity {
    private BoomViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBoomBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_boom);
        viewModel = new BoomViewModel(this, binding);
        binding.setViewModel(viewModel);
        viewModel.onCreate();
    }
}
