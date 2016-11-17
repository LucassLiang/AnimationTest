package mvvm.viewmodel;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.example.lucas.animationtest.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mvvm.adapter.ChatAdapter;
import util.StringUtil;

/**
 * Created by lucas on 16/11/2016.
 */

public class ChatViewModel implements SwipeRefreshLayout.OnRefreshListener {
    private Activity context;
    private ActivityChatBinding binding;
    private ChatAdapter chatAdapter;
    private ChatItemViewModel chatItemViewModel;
    private LinearLayoutManager layoutManager;
    private AVIMConversation conversation = null;

    public ChatViewModel(Activity context, ActivityChatBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    public void onCreate() {
        initRecyclerView();
        getConversation("to");
    }

    private void initRecyclerView() {
        binding.srlChatList.setOnRefreshListener(this);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvChatList.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(context);
        binding.rvChatList.setAdapter(chatAdapter);
    }

    private void getConversation(final String toName) {
        final AVIMClient client = AVIMClient.getInstance("author");
        client.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                AVIMConversationQuery query = client.getQuery();
                query.withMembers(Arrays.asList(toName));
                query.whereEqualTo("customConversation", "author");
                query.findInBackground(new AVIMConversationQueryCallback() {
                    @Override
                    public void done(List<AVIMConversation> list, AVIMException e) {
                        if (handleExcept(e)) return;
                        if (null != list && list.size() > 0) {
                            conversation = list.get(0);
                        } else {
                            createNewConversation(client, toName, null);
                        }
                    }
                });
            }
        });
    }

    public void sendMessage(View view) {
        String content = binding.etContent.getText().toString().trim();
        if (StringUtil.isEmpty(content) || content.isEmpty()) {
            return;
        }
        sendByLeanCloud(content);
        binding.etContent.setText("");
        chatAdapter.notifyDataSetChanged();
    }

    private void sendByLeanCloud(final String content) {
        final AVIMClient author = AVIMClient.getInstance("author");
        author.open(new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVIMException e) {
                            if (handleExcept(e)) return;
                            if (conversation == null) {
                                createNewConversation(author, content, content);
                            } else {
                                sendMsg(content);
                            }
                        }
                    }
        );
    }

    private void sendMsg(final String content) {
        final AVIMMessage msg = new AVIMMessage();
        msg.setContent(content);
        msg.setTimestamp(System.currentTimeMillis());
        conversation.sendMessage(msg, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
//                if (handleExcept(e)) return;
                chatItemViewModel = new ChatItemViewModel(msg);
                chatAdapter.add(chatItemViewModel);
                chatAdapter.notifyDataSetChanged();
                layoutManager.scrollToPositionWithOffset(chatAdapter.size() - 1, 0);
            }
        });
    }

    private void createNewConversation(AVIMClient client, String toName, final String content) {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("customConversation", "author");
        client.createConversation(Arrays.asList(toName), "chatting", attributes, false, true,
                new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        if (handleExcept(e)) return;
                        conversation = avimConversation;
                        if (!StringUtil.isEmpty(content)) {
                            sendMsg(content);
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        final AVIMClient client = AVIMClient.getInstance("author");
        client.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (handleExcept(e)) return;
                if (conversation == null) {
                    binding.srlChatList.setRefreshing(false);
                } else {
                    getHistory(conversation);
                }
            }
        });
    }

    private void getHistory(AVIMConversation conversation) {
        conversation.queryMessages(new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if (handleExcept(e)) return;
                chatAdapter.clear();
                List<ChatItemViewModel> viewModels = new ArrayList<ChatItemViewModel>();
                for (AVIMMessage msg : list) {
                    chatItemViewModel = new ChatItemViewModel(msg);
                    viewModels.add(chatItemViewModel);
                }
                chatAdapter.addAll(0, viewModels);
                chatAdapter.notifyDataSetChanged();
                binding.srlChatList.setRefreshing(false);
            }
        });
    }

    private boolean handleExcept(AVIMException e) {
        if (null != e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }
}
