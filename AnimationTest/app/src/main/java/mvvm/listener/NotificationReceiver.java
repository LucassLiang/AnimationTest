package mvvm.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import constant.Constant;
import mvvm.view.ChatActivity;
import mvvm.view.LoginActivity;
import util.StringUtil;

/**
 * Created by lucas on 18/11/2016.
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (AvImClientManager.getInstance().getClient() != null) {
            String clientId = AvImClientManager.getInstance().getClientId();
            if (StringUtil.isEmpty(clientId)) {
                return;
            }
            goToChatGroup(context, intent);
        } else {
            goToLogin(context);
        }
    }

    private void goToChatGroup(Context context, Intent intent) {
        Intent startActivityIntent = new Intent(context, ChatActivity.class);
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityIntent.putExtra(Constant.ID, intent.getStringExtra(Constant.MEMBER_ID));
        context.startActivity(startActivityIntent);
    }

    private void goToLogin(Context context) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loginIntent);
    }
}
