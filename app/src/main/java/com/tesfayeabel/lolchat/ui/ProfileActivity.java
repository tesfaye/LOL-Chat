package com.tesfayeabel.lolchat.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.ChatService;
import com.tesfayeabel.lolchat.LOLChatApplication;
import com.tesfayeabel.lolchat.R;
import com.tesfayeabel.lolchat.ui.adapter.RecentGamesAdapter;

import java.util.List;

import jriot.main.JRiot;
import jriot.main.JRiotException;
import jriot.objects.Game;
import jriot.objects.Summoner;

public class ProfileActivity extends Activity implements ServiceConnection {
    private String playerName;
    private ExpandableListView recentGames;
    private ImageView imageView;
    private TextView level;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        playerName = getIntent().getStringExtra("player");
        recentGames = (ExpandableListView) findViewById(R.id.listView);
        imageView = (ImageView) findViewById(R.id.gameavatar);
        TextView textView = (TextView) findViewById(R.id.name);
        textView.setText(playerName);
        level = (TextView) findViewById(R.id.level);
        bindService(new Intent(this, ChatService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(final ComponentName name, final IBinder service) {
        LolChat lolChat = ((ChatService.LocalBinder) service).getService().getLolChat();
        final JRiot jRiot = lolChat.getRiotApi();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Summoner summoner = jRiot.getSummoner(playerName.replace(" ", ""));
                    final List<Game> games = jRiot.getRecentGames(summoner.getId()).getGames();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recentGames.setAdapter(new RecentGamesAdapter(getApplicationContext(), games));
                            level.setText("Level " + (int) summoner.getSummonerLevel());
                            Picasso.with(getApplicationContext()).load(LOLChatApplication.getRiotResourceURL() + "/img/profileicon/" + summoner.getProfileIconId() + ".png").into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                                    progressBar.setVisibility(View.GONE);
                                    imageView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError() {
                                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                                    progressBar.setVisibility(View.GONE);
                                    imageView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                } catch (JRiotException exception) {
                    exception.printStackTrace();
                }
            }
        }).start();
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
