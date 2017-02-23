package mvvm.view;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.lucas.animationtest.R;
import com.example.lucas.animationtest.databinding.ActivityCaptureBinding;

import mvvm.viewmodel.CaptureVModel;

/**
 * Created by lucas on 2017/2/23.
 */

public class CustomCaptureActivity extends Activity {
    private CaptureVModel captureVModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCaptureBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_capture);
        captureVModel = new CaptureVModel(binding, this);
        captureVModel.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureVModel.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        captureVModel.onStop();
    }
}
