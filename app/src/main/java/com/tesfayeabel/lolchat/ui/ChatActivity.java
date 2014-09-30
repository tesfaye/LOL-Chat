package com.tesfayeabel.lolchat.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.tesfayeabel.lolchat.ChatService;
import com.tesfayeabel.lolchat.Message;
import com.tesfayeabel.lolchat.R;
import com.tesfayeabel.lolchat.ui.adapter.MessageAdapter;

import java.util.ArrayList;

public class ChatActivity extends Activity implements ServiceConnection, SharedPreferences.OnSharedPreferenceChangeListener {
    private String friendName;
    private Friend friend;
    private ListView conversation;
    private EditText messageBox;
    private ChatService chatService;
    private SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lolchat_chat);
        messageBox = (EditText) findViewById(R.id.textinput);
        ImageView send = (ImageView) findViewById(R.id.textSendButton);
        conversation = (ListView) findViewById(R.id.messages_view);
        sharedPreferences = getSharedPreferences("messageHistory", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        messageBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });
        friendName = getIntent().getStringExtra("friend");
        setTitle(friendName);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        if (savedInstanceState != null) {
            conversation.setAdapter(new MessageAdapter(this, (ArrayList) savedInstanceState.getParcelableArrayList("messages")));
            conversation.onRestoreInstanceState(savedInstanceState.getParcelable("listView"));
        } else {
            conversation.setAdapter(new MessageAdapter(this));
        }
        bindService(new Intent(this, ChatService.class), this, Context.BIND_AUTO_CREATE);
    }

    private void sendMessage() {
        String message = messageBox.getText().toString();
        if (friend != null && !message.isEmpty()) {
            friend.sendMessage(message);
            chatService.saveMessage(new Message(friend.getName(), message.replace("\n", " "), MessageAdapter.DIRECTION_OUTGOING, System.currentTimeMillis()));
            messageBox.setText("");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_clear:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(friendName).apply();
                conversation.setAdapter(new MessageAdapter(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onServiceConnected(final ComponentName name, final IBinder service) {
        chatService = ((ChatService.LocalBinder) service).getService();
        LolChat lolChat = chatService.getLolChat();
        friend = lolChat.getFriendByName(friendName);
        MessageAdapter adapter = (MessageAdapter) conversation.getAdapter();
        adapter.setFriendProfileIcon(friend.getStatus().getProfileIconId());
        adapter.setMyProfileIcon(lolChat.getConnectedSummoner().getProfileIconId());
        adapter.notifyDataSetChanged();//force update of view
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        unbindService(this);
    }

    @Override
    public void onServiceDisconnected(final ComponentName name) {
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        if(key.equals(friendName)) {
            String message = preferences.getString(key, null);
            if (message != null) {
                String[] messages = message.split("\n");
                MessageAdapter adapter = (MessageAdapter) conversation.getAdapter();
                adapter.addMessage(new Message(messages[messages.length - 1]));
                conversation.setSelection(adapter.getCount() - 1);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("listView", conversation.onSaveInstanceState());
        savedInstanceState.putParcelableArrayList("messages", (ArrayList<Message>) ((MessageAdapter) conversation.getAdapter()).getMessages());
    }

    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("messageHistory", Context.MODE_PRIVATE);
        String messages = preferences.getString(friendName, null);
        if (messages != null) {
            String[] text = messages.split("\n");
            MessageAdapter adapter = (MessageAdapter) conversation.getAdapter();
            adapter.clear();
            for (String s : text) {
                adapter.addMessage(new Message(s));
            }
            conversation.setSelection(adapter.getCount() - 1);
        }
    }
}
