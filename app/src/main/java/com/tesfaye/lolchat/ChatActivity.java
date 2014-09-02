package com.tesfaye.lolchat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatActivity extends Activity implements ServiceConnection, ChatListener
{
    private String friendName;
    private Friend friend;
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
                    friend.sendMessage(message, ChatActivity.this);
                    MessageAdapter adapter = (MessageAdapter)conversation.getAdapter();
                    adapter.addMessage("Me: " + message, MessageAdapter.DIRECTION_OUTGOING);
                    conversation.setSelection(adapter.getCount() - 1);
                    messageBox.setText("");
                }
            }
        });
        if(savedInstanceState != null)
        {
            conversation.setAdapter(new MessageAdapter(this, (ArrayList)savedInstanceState.getSerializable("messages")));
            conversation.onRestoreInstanceState(savedInstanceState.getParcelable("listView"));
        }else
        {
            conversation.setAdapter(new MessageAdapter(this));
        }
        bindService(new Intent(this, ChatService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(final ComponentName name, final IBinder service) {
        ChatService chatService = ((ChatService.LocalBinder) service).getService();
        LolChat lolChat = chatService.getLolChat();
        friend = lolChat.getFriendByName(friendName);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                adapter.addMessage(friend.getName() + ": " + message, MessageAdapter.DIRECTION_INCOMING);
                conversation.setSelection(adapter.getCount() - 1);
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("listView", conversation.onSaveInstanceState());
        savedInstanceState.putSerializable("messages", (ArrayList)((MessageAdapter)conversation.getAdapter()).getMessages());
    }
    public void onPause()
    {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("messageHistory", Context.MODE_PRIVATE).edit();
        StringBuilder history = new StringBuilder();
        List<Pair<String, Integer>> messages = ((MessageAdapter)conversation.getAdapter()).getMessages();
        for(int i = 0; i < messages.size(); i++)
        {
            history.append(messages.get(i).second + messages.get(i).first);
            if(i < messages.size() -1)
                history.append("\n");
        }
        editor.putString(friendName + "History", history.toString());
        editor.apply();
    }
    public void onResume()
    {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("messageHistory", Context.MODE_PRIVATE);
        String messages = preferences.getString(friendName + "History", null);
        if (messages != null) {
            String[] text = messages.split("\n");
            if(text.length > 1) {
                System.out.println(text.length);
                ArrayList<Pair<String, Integer>> list = new ArrayList<Pair<String, Integer>>();
                for (String s : text) {
                    int i = Character.getNumericValue(s.charAt(0));
                    list.add(new Pair(s.substring(1), i));
                }
                conversation.setAdapter(new MessageAdapter(this, list));
                conversation.setSelection(list.size() - 1);
            }
        }
    }
}
