package com.tesfayeabel.lolchat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.adapter.MessageAdapter;

public class ProfileActivity extends Activity implements ServiceConnection {
    private String friendName;
    private ListView recentGames;
    private LolChat lolChat;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        friendName = getIntent().getStringExtra("friend");
        recentGames = (ListView) findViewById(R.id.listView);
        recentGames.setAdapter(new MessageAdapter(this));

        TextView textView = (TextView) findViewById(R.id.name);
        textView.setText(friendName);
    }
    @Override
    public void onServiceConnected(final ComponentName name, final IBinder service) {
        lolChat = ((ChatService.LocalBinder) service).getService().getLolChat();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(getApplicationContext()).load("http://ddragon.leagueoflegends.com/cdn/4.14.2/img/profileicon/" + lolChat.getFriendByName(friendName).getStatus().getProfileIconId() + ".png").into(imageView);
    }
    @Override
    public void onServiceDisconnected(final ComponentName name) {
    }
}
