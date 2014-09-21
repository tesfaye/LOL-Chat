package com.tesfayeabel.lolchat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.tesfayeabel.lolchat.R;

/**
 * Created by Abel Tesfaye on 9/21/2014.
 */
public class ConversationsFragment extends LOLChatFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lolchat_conversations, container, false);
        return view;
    }

    public void onChatConnected(final LolChat chat) {

    }
}
