package com.tesfaye.lolchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Abel Tesfaye on 8/24/2014.
 */
public class LoginActivity extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_lolchat_login);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = ((EditText) findViewById(R.id.section_label)).getText().toString();
                final String password = ((EditText) findViewById(R.id.section_label1)).getText().toString();
                //((LOLChatMain)getActivity()).bind(username, password);
                startActivity(new Intent(getApplicationContext(), LOLChatMain.class));
            }
        });
    }
}