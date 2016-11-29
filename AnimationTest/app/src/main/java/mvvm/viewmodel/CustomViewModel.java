package mvvm.viewmodel;

import android.content.Context;
import android.view.View;

import com.example.lucas.animationtest.databinding.ActivityCustomBinding;

/**
 * Created by lucas on 16/11/2016.
 */

public class CustomViewModel {
    private Context context;
    private ActivityCustomBinding binding;

    public CustomViewModel(Context context, ActivityCustomBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    public void startAnimation(View view) {
//        binding.viewCustom.startAnimation(1000);
        if (binding.viewLoading.isOver()) {
            binding.viewLoading.startLoading();
        } else {
            binding.viewLoading.setOver(true);
        }
    }
}
