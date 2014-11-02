package com.tesfayeabel.lolchat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tesfayeabel.lolchat.R;

/**
 * Created by Abel Tesfaye on 9/28/2014.
 */
public class AboutActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ListView listView = (ListView) findViewById(R.id.listView);
        String[] licenses = getResources().getStringArray(R.array.licenses);
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, licenses));
    }
}
