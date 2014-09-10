package com.tesfayeabel.lolchat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.tesfayeabel.lolchat.adapter.MessageAdapter;

public class ProfileActivity extends Activity {
    private String friendName;
    private ListView recentGames;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        friendName = getIntent().getStringExtra("friend");
        recentGames = (ListView) findViewById(R.id.listView);
        recentGames.setAdapter(new MessageAdapter(this));
        TextView textView = (TextView) findViewById(R.id.name);
        textView.setText(friendName);
    }
}
