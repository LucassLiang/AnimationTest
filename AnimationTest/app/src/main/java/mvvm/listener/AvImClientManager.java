package mvvm.listener;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import util.StringUtil;

/**
 * Created by lucas on 18/11/2016.
 */

public class AvImClientManager {
    private AvImClientManager manager;
    private AVIMClient client;
    private String clientId = "";

    public AvImClientManager() {

    }

    public synchronized AvImClientManager getInstance() {
        if (null == manager) {
            manager = new AvImClientManager();
        }
        return manager;
    }

    public void open(String clientId, AVIMClientCallback callback) {
        this.clientId = clientId;
        client = AVIMClient.getInstance(clientId);
        client.open(callback);
    }

    public AVIMClient getClient() {
        return client;
    }

    public String getClientId() {
        if (StringUtil.isEmpty(clientId)) {
            throw new IllegalStateException("Please call AvImClientManager first!");
        }
        return clientId;
    }
}
