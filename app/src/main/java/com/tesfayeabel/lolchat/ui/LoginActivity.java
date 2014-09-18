package com.tesfayeabel.lolchat.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.theholywaffle.lolchatapi.ChatServer;
import com.tesfayeabel.lolchat.ChatService;
import com.tesfayeabel.lolchat.LOLChatMain;
import com.tesfayeabel.lolchat.LoginCallBack;
import com.tesfayeabel.lolchat.R;

import java.util.ArrayList;

public class LoginActivity extends Activity implements LoginCallBack {
    private LinearLayout pbLayout;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private CheckBox rememberBox;
    private Spinner serverList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lolchat_login);
        SharedPreferences sharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        pbLayout = (LinearLayout) findViewById(R.id.pbLayout);
        usernameEdit = ((EditText) findViewById(R.id.usernameBox));
        passwordEdit = (EditText) findViewById(R.id.passwordBox);
        rememberBox = (CheckBox) findViewById(R.id.rememberbox);
        serverList = (Spinner) findViewById(R.id.serverlist);
        Button connect = (Button) findViewById(R.id.connectButton);
        TextView passwordForgot = (TextView) findViewById(R.id.forgot_Button);
        passwordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://na.leagueoflegends.com/account/recovery/password"));
                startActivity(browserIntent);
            }
        });
        ArrayList<String> serverArrayList = new ArrayList<String>();
        for (ChatServer server : ChatServer.getChatServersWithAPI()) {
            serverArrayList.add(server.name);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, serverArrayList);
        serverList.setPromptId(R.string.server_select);
        serverList.setAdapter(spinnerArrayAdapter);
        serverList.setSelection(spinnerArrayAdapter.getPosition(sharedPreferences.getString("server", "North America")));
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    return true;
                }
                return false;
            }
        });
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);
        String server = sharedPreferences.getString("server", null);
        if (username != null && password != null && server != null) {
            usernameEdit.setText(username);
            passwordEdit.setText(password);
            rememberBox.setChecked(true);
        }
    }

    private void login() {
        pbLayout.setVisibility(View.VISIBLE);
        Intent service = new Intent(this, ChatService.class);
        service.putExtra("username", usernameEdit.getText().toString());
        service.putExtra("password", passwordEdit.getText().toString());
        service.putExtra("server", serverList.getSelectedItem().toString());
        service.putExtra("savePassword", rememberBox.isChecked());
        ChatService.callBack = this;
        startService(service);
    }

    public void onLogin(boolean successful) {
        if (successful) {
            Intent intent = new Intent(getApplicationContext(), LOLChatMain.class);
            startActivity(intent);
        } else {
            stopService(new Intent(getApplicationContext(), ChatService.class));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbLayout.setVisibility(View.GONE);
            }
        });
    }
}