package com.tesfaye.lolchat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public class ChatActivity extends Activity implements ServiceConnection
{
    private String friendName;
    private Friend friend;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lolchat_chat);
        final EditText messageBox = (EditText) findViewById(R.id.messageBox);
        Button send = (Button) findViewById(R.id.messageSend);
        friendName = getIntent().getStringExtra("friend");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(friend != null)
                {
                    friend.sendMessage(messageBox.getText().toString());
                    messageBox.setText("");
                }
            }
        });
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
}
