package mvvm.viewmodel;

import android.content.Context;

import com.example.lucas.animationtest.databinding.ActivityMapBinding;

/**
 * Created by lucas on 2016/11/28.
 */

public class MapVModel {
    private ActivityMapBinding binding;
    private Context context;

    public MapVModel(Context context, ActivityMapBinding binding) {
        this.context = context;
        this.binding = binding;
    }
}
