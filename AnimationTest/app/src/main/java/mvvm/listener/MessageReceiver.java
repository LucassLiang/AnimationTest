package mvvm.listener;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;

import org.greenrobot.eventbus.EventBus;

import mvvm.model.MessageEvent;

/**
 * Created by lucas on 18/11/2016.
 */

public class MessageReceiver extends AVIMTypedMessageHandler<AVIMTypedMessage> {
    private Context context;

    public MessageReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessage(message, conversation, client);
        String clientId = "";
        Log.i("TAG", "onMessage: " + "receive");
        try {
            clientId = new AvImClientManager().getInstance().getClientId();
            if (client.getClientId().equals(clientId)) {
                if (!message.getFrom().equals(clientId)) {
                    MessageEvent event = new MessageEvent(message, conversation);
                    EventBus.getDefault().post(event);
                }
            } else {
                client.close(null);
            }
        } catch (IllegalStateException ex) {
            client.close(null);
        }
    }
}
