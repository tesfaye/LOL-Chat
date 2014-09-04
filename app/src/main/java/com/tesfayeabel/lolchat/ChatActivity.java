package com.tesfayeabel.lolchat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity implements ServiceConnection, ChatListener
{
    private String friendName;
    private Friend friend;
    private ChatService chatService;
    private ListView conversation;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lolchat_chat);
        final EditText messageBox = (EditText) findViewById(R.id.messageBox);
        Button send = (Button) findViewById(R.id.messageSend);
        conversation = (ListView) findViewById(R.id.listView);
        friendName = getIntent().getStringExtra("friend");
        setTitle(friendName);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageBox.getText().toString();
                if(friend != null && !message.isEmpty())
                {
                    friend.sendMessage(message);
                    MessageAdapter adapter = (MessageAdapter)conversation.getAdapter();
                    adapter.addMessage(new Message("Me", message, MessageAdapter.DIRECTION_OUTGOING));
                    conversation.setSelection(adapter.getCount() - 1);
                    messageBox.setText("");
                }
            }
        });
        if(savedInstanceState != null)
        {
            conversation.setAdapter(new MessageAdapter(this, (ArrayList)savedInstanceState.getParcelableArrayList("messages")));
            conversation.onRestoreInstanceState(savedInstanceState.getParcelable("listView"));
        }else
        {
            conversation.setAdapter(new MessageAdapter(this));
        }
        bindService(new Intent(this, ChatService.class), this, Context.BIND_AUTO_CREATE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_clear:
                SharedPreferences.Editor editor = getSharedPreferences("messageHistory", Context.MODE_PRIVATE).edit();
                editor.remove(friendName + "History").apply();
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
        chatService.chatListener = this;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatService.chatListener = null;
        chatService = null;
        unbindService(this);
    }
    @Override
    public void onServiceDisconnected(final ComponentName name) {}

    @Override
    public void onMessage(final Friend friend, final String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MessageAdapter adapter = (MessageAdapter)conversation.getAdapter();
                adapter.addMessage(new Message(friend.getName(), message, MessageAdapter.DIRECTION_INCOMING));
                conversation.setSelection(adapter.getCount() - 1);
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("listView", conversation.onSaveInstanceState());
        savedInstanceState.putParcelableArrayList("messages", (ArrayList<Message>)((MessageAdapter)conversation.getAdapter()).getMessages());
    }
    public void onPause()
    {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("messageHistory", Context.MODE_PRIVATE).edit();
        List<Message> messages = ((MessageAdapter)conversation.getAdapter()).getMessages();
        if(messages.size() > 0) {
            StringBuilder history = new StringBuilder();
            for (int i = 0; i < messages.size(); i++) {
                history.append(messages.get(i));
                if (i < messages.size() - 1)
                    history.append("\n");
            }
            editor.putString(friendName + "History", history.toString());
            editor.apply();
        }
    }
    public void onResume()
    {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("messageHistory", Context.MODE_PRIVATE);
        String messages = preferences.getString(friendName + "History", null);
        if (messages != null) {
            String[] text = messages.split("\n");
            ArrayList<Message> list = new ArrayList<Message>();
            for (String s : text) {
                list.add(new Message(s));
            }
            conversation.setAdapter(new MessageAdapter(this, list));
            conversation.setSelection(list.size() - 1);
        }
    }
}
