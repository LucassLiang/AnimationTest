package mvvm.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import constant.Constant;
import mvvm.view.ChatActivity;
import util.StringUtil;

/**
 * Created by lucas on 18/11/2016.
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != AvImClientManager.getInstance().getClient()) {
            String clientId = AvImClientManager.getInstance().getClientId();
            if (StringUtil.isEmpty(clientId)) {
                return;
            }
            goToSingleChatPage(context, intent);
        }
    }

    private void goToSingleChatPage(Context context, Intent intent) {
        Intent startActivityIntent = new Intent(context, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.MEMBER_ID, intent.getStringExtra(Constant.MEMBER_ID));
        context.startActivity(startActivityIntent);
    }
}
