package util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.example.lucas.animationtest.R;

import mvvm.listener.MessageReceiver;

/**
 * Created by lucas on 21/11/2016.
 */

public class BaseApp extends Application {
    private static BaseApp baseApp;

    public static <T extends BaseApp> T BaseApp() {
        return (T) baseApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApp = this;
        AVOSCloud.initialize(this, getString(R.string.avos_app_id), getString(R.string.avos_app_key));

        AVIMMessageManager.registerDefaultMessageHandler(new MessageReceiver(this));
    }

    //get current show activity name
    public static String getCurrentActivityName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ComponentName componentName = manager.getRunningTasks(1).get(0).topActivity;

        String activityRoute = componentName.getShortClassName();
        return activityRoute.substring(activityRoute.lastIndexOf('.') + 1, activityRoute.length());
    }
}
