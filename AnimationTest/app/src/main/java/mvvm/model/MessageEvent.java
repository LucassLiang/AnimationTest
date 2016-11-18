package mvvm.model;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

/**
 * Created by lucas on 18/11/2016.
 */

public class MessageEvent {
    private AVIMTypedMessage message;
    private AVIMConversation conversation;

    public MessageEvent(AVIMTypedMessage message, AVIMConversation conversation) {
        this.message = message;
        this.conversation = conversation;
    }

    public AVIMTypedMessage getMessage() {
        return message;
    }

    public void setMessage(AVIMTypedMessage message) {
        this.message = message;
    }

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }
}
