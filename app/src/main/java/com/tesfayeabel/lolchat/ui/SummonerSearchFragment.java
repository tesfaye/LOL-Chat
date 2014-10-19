package com.tesfayeabel.lolchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.tesfayeabel.lolchat.R;

import jriot.main.JRiotException;
import jriot.objects.Summoner;

/**
 * Created by Abel Tesfaye on 9/14/2014.
 */
public class SummonerSearchFragment extends LOLChatFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lolchat_search, container, false);
        Button button = (Button) view.findViewById(R.id.button);
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lookUpSummoner(editText.getText().toString());
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    lookUpSummoner(textView.getText().toString());
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onChatConnected(final LolChat chat) {
    }

    private void lookUpSummoner(final String name) {
        final LOLChatActivity activity = (LOLChatActivity) getActivity();
        if (activity.getLolChat() != null && !name.isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final Summoner summoner = activity.getLolChat().getRiotApi().getSummoner(name.replace(" ", ""));
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (summoner != null) {
                                    Intent intent = new Intent(activity, ProfileActivity.class);
                                    intent.putExtra("player", summoner.getName());
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(activity, "Summoner not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (JRiotException e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "Summoner not found", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }
    }
}