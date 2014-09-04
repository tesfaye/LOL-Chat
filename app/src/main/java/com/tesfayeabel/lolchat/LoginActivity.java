package com.tesfayeabel.lolchat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.theholywaffle.lolchatapi.ChatServer;

import java.util.ArrayList;

public class LoginActivity extends Activity implements ServiceConnection, LoginCallBack
{
    private String username, password, server;
    private boolean savePassword;
    private LinearLayout pbLayout;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lolchat_login);
        SharedPreferences sharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
        password = sharedPreferences.getString("password", null);
        pbLayout = (LinearLayout)findViewById(R.id.pbLayout);
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
        ArrayList<String> serverArrayList = new ArrayList<String>();
        for(ChatServer server: ChatServer.getChatServersWithAPI())
        {
            serverArrayList.add(server.name);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, serverArrayList);
        serverList.setAdapter(spinnerArrayAdapter);
        serverList.setSelection(spinnerArrayAdapter.getPosition(sharedPreferences.getString("server", "North America")));
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = ((EditText) findViewById(R.id.usernameBox)).getText().toString();
                password = ((EditText) findViewById(R.id.passwordBox)).getText().toString();
                savePassword = ((CheckBox) findViewById(R.id.rememberbox)).isChecked();
                server = ((Spinner) findViewById(R.id.serverlist)).getSelectedItem().toString();
                bind();
            }
        });
        if(username != null && password != null)
        {
            savePassword = true;//remember to save password again
            bind();
        }
    }
    public void bind()
    {
        pbLayout.setVisibility(View.VISIBLE);
        bindService(new Intent(this, ChatService.class), this, Context.BIND_AUTO_CREATE);
    }
    @Override
    public void onServiceConnected(final ComponentName name, final IBinder service) {
        ChatService chatService = ((ChatService.LocalBinder) service).getService();
        chatService.connectLOLChat(username, password, server, this);
    }

    @Override
    public void onServiceDisconnected(final ComponentName name)
    {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(this);
        }catch(Exception e)
        {
            //exception thrown if unbind is called when no service is bound
        }
    }
    public void onLogin(boolean successful)
    {
        if(successful)
        {
            SharedPreferences.Editor editor = getSharedPreferences("loginData", Context.MODE_PRIVATE).edit();//TODO: ENCRYPTION
            if(savePassword)
            {
                editor.putString("username", username);
                editor.putString("password", password);
            }
            editor.putString("server", server);
            editor.apply();
            Intent intent = new Intent(this, LOLChatMain.class);
            startActivity(intent);
        }else
        {
            unbindService(this);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbLayout.setVisibility(View.GONE);
            }
        });
    }
}