package mvvm.viewmodel;

import com.avos.avoscloud.im.v2.AVIMMessage;

/**
 * Created by lucas on 16/11/2016.
 */

public class ChatItemViewModel {
    private AVIMMessage message;

    public ChatItemViewModel(AVIMMessage message) {
        this.message = message;
    }

    public boolean isAuthor() {
        if (message.getFrom().equals("author")) {
            return true;
        }
        return false;
    }

    public AVIMMessage getMessage() {
        return message;
    }

    public void setMessage(AVIMMessage message) {
        this.message = message;
    }
}
