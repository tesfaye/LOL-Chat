package com.tesfaye.lolchat;

import android.app.Fragment;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.util.ArrayList;

public class MainFragment extends Fragment
{
    private ListView listView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_lolchat_main, container, false);
        listView  = (ListView) view.findViewById(R.id.listView);
        return view;
    }
    public void onChatConnected(ChatService chatService) {
        LolChat chat = chatService.getLolChat();
        listView.setAdapter(new FriendViewAdapter(getActivity(), R.layout.friend_row, chat.getOnlineFriends()));
    }
}
