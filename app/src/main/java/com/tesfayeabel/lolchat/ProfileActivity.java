package com.tesfayeabel.lolchat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.adapter.RecentGamesAdapter;

import java.util.List;

import jriot.main.JRiot;
import jriot.main.JRiotException;
import jriot.objects.Game;

public class ProfileActivity extends Activity implements ServiceConnection {
    private String friendName;
    private ListView recentGames;
    private ImageView imageView;
    private TextView level;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        friendName = getIntent().getStringExtra("friend");
        recentGames = (ListView) findViewById(R.id.listView);
        imageView = (ImageView) findViewById(R.id.gameavatar);
        TextView textView = (TextView) findViewById(R.id.name);
        textView.setText(friendName);
        level = (TextView) findViewById(R.id.level);
        bindService(new Intent(this, ChatService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(final ComponentName name, final IBinder service) {
        LolChat lolChat = ((ChatService.LocalBinder) service).getService().getLolChat();
        final JRiot jRiot = lolChat.getRiotApi();
        Friend friend = lolChat.getFriendByName(friendName);
        LolStatus status = friend.getStatus();
        level.setText("Level: " + status.getLevel());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Game> games = jRiot.getRecentGames(jRiot.getSummoner(friendName).getId()).getGames();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recentGames.setAdapter(new RecentGamesAdapter(getApplicationContext(), games));
                        }
                    });
                } catch (JRiotException exception) {
                    exception.printStackTrace();
                }
            }
        }).start();
        Picasso.with(getApplicationContext()).load(LOLChatApplication.getRiotResourceURL() + "/img/profileicon/" + status.getProfileIconId() + ".png").into(imageView);
    }

    @Override
    public void onServiceDisconnected(final ComponentName name) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }
}
