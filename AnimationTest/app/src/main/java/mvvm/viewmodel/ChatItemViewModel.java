package mvvm.viewmodel;

import com.avos.avoscloud.im.v2.AVIMMessage;

import java.text.SimpleDateFormat;

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

    public String getTime() {
        long timestamp = getMessage().getTimestamp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh-mm");
        return format.format(timestamp);
    }

    public AVIMMessage getMessage() {
        return message;
    }

    public void setMessage(AVIMMessage message) {
        this.message = message;
    }
}
