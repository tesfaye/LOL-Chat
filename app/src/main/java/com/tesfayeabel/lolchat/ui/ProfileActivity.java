/*
 * LOL-Chat
 * Copyright (C) 2014  Abel Tesfaye
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
                            Picasso.with(getApplicationContext()).load(LOLChatApplication.getProfileIconURL(summoner.getProfileIconId())).into(imageView, new Callback() {
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
