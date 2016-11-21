package mvvm.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityChatBinding;

import mvvm.viewmodel.ChatViewModel;

/**
 * Created by lucas on 16/11/2016.
 */

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private ChatViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Chatting Room");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        viewModel = new ChatViewModel(this, binding);
        viewModel.onCreate();
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
