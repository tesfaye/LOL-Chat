package com.tesfaye.lolchat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.theholywaffle.lolchatapi.ChatServer;

import java.util.ArrayList;

public class LoginActivity extends Activity
{
    private String username, password;
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

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, serverArrayList);
        serverList.setAdapter(spinnerArrayAdapter);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = ((EditText) findViewById(R.id.usernameBox)).getText().toString();
                password = ((EditText) findViewById(R.id.passwordBox)).getText().toString();
                bind();
            }
        });
    }
    public void bind()
    {
        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    public void unbind()
    {
        unbindService(mConnection);
    }
    public void startMainActivity()
    {
        Intent intent = new Intent(this, LOLChatMain.class);
        startActivity(intent);
    }
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            ChatService mService = ((ChatService.LocalBinder) service).getService();
            if(mService.connectLOLChat(username, password))
            {
                startMainActivity();
                return;
            }
            unbind();
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {

        }
    };
}