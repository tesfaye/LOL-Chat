package com.tesfayeabel.lolchat.ui.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.tesfayeabel.lolchat.R;
import com.tesfayeabel.lolchat.ui.LOLChatFragment;

/**
 * Created by Abel Tesfaye on 9/20/2014.
 */
public class RecentConversations extends LOLChatFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lolchat_recent, container, false);
        return view;
    }

    @Override
    public void onChatConnected(final LolChat chat) {

    }
}
