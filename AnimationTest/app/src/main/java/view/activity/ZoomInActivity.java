package view.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by lucas on 11/7/16.
 */

public class ZoomInActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
