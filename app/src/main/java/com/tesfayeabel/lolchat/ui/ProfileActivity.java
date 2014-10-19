package com.tesfayeabel.lolchat.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.LOLChatApplication;
import com.tesfayeabel.lolchat.R;
import com.tesfayeabel.lolchat.ui.adapter.RecentGamesAdapter;

import java.util.List;

import jriot.main.JRiot;
import jriot.main.JRiotException;
import jriot.objects.Game;
import jriot.objects.Summoner;

public class ProfileActivity extends LOLChatActivity {
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
        level = (TextView) findViewById(R.id.level);
        ((TextView) findViewById(R.id.name)).setText(playerName);
    }

    @Override
    public void onChatConnected() {
        final JRiot jRiot = getLolChat().getRiotApi();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Summoner summoner = jRiot.getSummoner(playerName.replace(" ", ""));
                    final List<Game> games = jRiot.getRecentGames(summoner.getId()).getGames();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recentGames.setAdapter(new RecentGamesAdapter(ProfileActivity.this, games));
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
}
