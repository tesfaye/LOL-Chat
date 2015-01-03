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