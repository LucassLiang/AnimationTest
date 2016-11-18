package mvvm.model;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;

/**
 * Created by lucas on 18/11/2016.
 */

public class MessageEvent {
    private AVIMMessage message;
    private AVIMConversation conversation;

    public MessageEvent(AVIMMessage message, AVIMConversation conversation) {
        this.message = message;
        this.conversation = conversation;
    }

    public AVIMMessage getMessage() {
        return message;
    }

    public void setMessage(AVIMMessage message) {
        this.message = message;
    }

    public AVIMConversation getConversation() {
        return conversation;
    }

    public void setConversation(AVIMConversation conversation) {
        this.conversation = conversation;
    }
}
