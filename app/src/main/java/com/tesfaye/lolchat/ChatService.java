package com.tesfaye.lolchat;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;

import java.util.logging.Logger;

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
    public void onCreate()
    {
        super.onCreate();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                lolChat = new LolChat(ChatServer.NA, FriendRequestPolicy.REJECT_ALL);
                lolChat.login("dwadwa","awdwad");
            }
        });
        try {
            t.start();
            t.join();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText(getString(R.string.app_name) + " is running")
                .setContentTitle("USERNAME(STATUS)")
                .setTicker(getString(R.string.app_name) + " is now running")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();
        startForeground(69, notification);
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
