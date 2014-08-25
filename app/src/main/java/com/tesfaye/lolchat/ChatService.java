package com.tesfaye.lolchat;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;

import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.riotapi.RiotApiKey;

/**
 * Created by Abel Tesfaye on 8/23/2014.
 */
public class ChatService extends Service{
    private LolChat lolChat;
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public void connectLOLChat(final String username, final String password)
    {
        final Notification.Builder notification = new Notification.Builder(this);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                lolChat = new LolChat(ChatServer.NA, FriendRequestPolicy.REJECT_ALL, new RiotApiKey("99a0d299-2476-4539-901f-0fdd0598bcf8"));
                if(lolChat.login(username, password))
                {
                    notification
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentText(getString(R.string.app_name) + " is running")
                            .setContentTitle(lolChat.getConnectedUsername())
                            .setTicker(getString(R.string.app_name) + " is now running")
                            .setDefaults(Notification.DEFAULT_VIBRATE);
                    startForeground(69, notification.getNotification());
                }
            }
        });
        try {
            t.start();
            t.join();//wait for da thread to finish
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(lolChat != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lolChat.disconnect();
                }
            }).start();
        }
    }
    public class LocalBinder extends Binder {
        ChatService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ChatService.this;
        }
    }
    public LolChat getLolChat()
    {
        return lolChat;
    }

}