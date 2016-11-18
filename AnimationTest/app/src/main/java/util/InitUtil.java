package util;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.example.lucas.animationtest.R;

import mvvm.listener.MessageReceiver;

/**
 * Created by lucas on 17/11/2016.
 */

public class InitUtil extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, getString(R.string.avos_app_id), getString(R.string.avos_app_key));

        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageReceiver(this));
    }
}
