package com.tesfayeabel.lolchat;

import android.app.Fragment;

import com.github.theholywaffle.lolchatapi.LolChat;

public abstract class LOLChatFragment extends Fragment{
    public abstract void onChatConnected(LolChat chat);
}
