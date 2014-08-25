package com.tesfaye.lolchat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.theholywaffle.lolchatapi.ChatServer;

import java.util.ArrayList;

/**
 * Created by Abel Tesfaye on 8/24/2014.
 */
public class LoginActivity extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //check if password is saved, if so, start main activity with login info
        setContentView(R.layout.activity_lolchat_login);
        Button connect = (Button) findViewById(R.id.connectButton);
        TextView passwordForgot = (TextView) findViewById(R.id.forgot_Button);
        passwordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://na.leagueoflegends.com/account/recovery/password"));
                startActivity(browserIntent);
            }
        });
        Spinner serverList = (Spinner) findViewById(R.id.serverlist);
        ChatServer[] servers =  ChatServer.BR.getDeclaringClass().getEnumConstants();
        ArrayList<String> serverArrayList = new ArrayList<String>();
        for(ChatServer server: servers)
        {
            serverArrayList.add(server.toString().toUpperCase());
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, serverArrayList);
        serverList.setAdapter(spinnerArrayAdapter);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = ((EditText) findViewById(R.id.usernameBox)).getText().toString();
                final String password = ((EditText) findViewById(R.id.passwordBox)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), LOLChatMain.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });
    }
}