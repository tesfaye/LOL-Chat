package com.tesfayeabel.lolchat.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.tesfayeabel.lolchat.ChatService;

/**
 * Created by Abel Tesfaye on 10/5/2014.
 */
public abstract class LOLChatActivity extends Activity {

    private LolChat lolChat;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ChatService chatService = ((ChatService.LocalBinder) service).getService();
            lolChat = chatService.getLolChat();
            onChatConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    public abstract void onChatConnected();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    public LolChat getLolChat() {
        return lolChat;
    }
}
