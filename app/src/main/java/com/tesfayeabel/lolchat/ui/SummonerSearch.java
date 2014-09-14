package com.tesfayeabel.lolchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.tesfayeabel.lolchat.R;

import jriot.main.JRiotException;
import jriot.objects.Summoner;

/**
 * Created by Abel Tesfaye on 9/14/2014.
 */
public class SummonerSearch extends LOLChatFragment{

    private Button button;
    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_lolchat_search, container, false);
        button = (Button) view.findViewById(R.id.button);
        editText = (EditText) view.findViewById(R.id.editText);
        return view;
    }

    @Override
    public void onChatConnected(final LolChat chat) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String player = editText.getText().toString();
                if(!player.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                final Summoner summoner = chat.getRiotApi().getSummoner(player.replace(" ", ""));
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(summoner != null)
                                        {
                                            Intent intent = new Intent(getActivity(), ProfileActivity.class);
                                            intent.putExtra("player", player);
                                            startActivity(intent);
                                        }else
                                        {
                                            Toast.makeText(getActivity().getApplicationContext(), "Summoner not found", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } catch (JRiotException e)
                            {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplicationContext(), "Summoner not found", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });
    }
}