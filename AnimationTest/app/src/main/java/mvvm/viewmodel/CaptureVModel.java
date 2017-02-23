package mvvm.viewmodel;

import android.content.Context;

import com.example.lucas.animationtest.databinding.ActivityCaptureBinding;

/**
 * Created by lucas on 2017/2/23.
 */

public class CaptureVModel {
    private ActivityCaptureBinding binding;
    private Context mContext;

    public CaptureVModel(ActivityCaptureBinding binding, Context mContext) {
        this.binding = binding;
        this.mContext = mContext;
    }

    public void onCreate() {

    }

    public void onResume() {

    }

    public void onStop() {

    }
}
