package mvvm.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityNotificationBinding;

import mvvm.viewmodel.NotificationViewModel;

/**
 * Created by lucas on 10/31/16.
 */

public class NotificationActivity extends AppCompatActivity {
    private ActivityNotificationBinding binding;
    private NotificationViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        viewModel = new NotificationViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
