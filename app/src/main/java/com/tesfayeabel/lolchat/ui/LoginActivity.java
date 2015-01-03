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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.tesfayeabel.lolchat.R;

import java.util.ArrayList;

public class LoginActivity extends Activity {
    private LinearLayout pbLayout;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private CheckBox rememberBox;
    private Spinner serverList;

    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pbLayout.setVisibility(View.GONE);
                }
            });
        }
    };

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
        for (ChatServer server : ChatServer.values()) {
            if (server.api != null)
                serverArrayList.add(server.name);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, serverArrayList);
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
        if (username != null && password != null) {
            usernameEdit.setText(username);
            passwordEdit.setText(password);
            rememberBox.setChecked(true);
        }
        IntentFilter filter = new IntentFilter("com.tesfayeabel.lolchat.ui.LoginActivity.LOGIN");
        registerReceiver(myBroadcastReceiver, filter);
    }

    private void login() {
        pbLayout.setVisibility(View.VISIBLE);
        Intent service = new Intent(this, ChatService.class);
        service.putExtra("username", usernameEdit.getText().toString());
        service.putExtra("password", passwordEdit.getText().toString());
        service.putExtra("server", serverList.getSelectedItem().toString());
        service.putExtra("savePassword", rememberBox.isChecked());
        startService(service);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
}