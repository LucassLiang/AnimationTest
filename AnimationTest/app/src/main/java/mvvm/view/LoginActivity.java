package mvvm.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityLoginBinding;

import mvvm.viewmodel.LoginViewModel;

/**
 * Created by lucas on 18/11/2016.
 */

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = new LoginViewModel(this,binding);
        binding.setViewModel(viewModel);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
