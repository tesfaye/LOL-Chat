package com.tesfaye.lolchat;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        List<Friend> friends = chat.getFriends();
        Collections.sort(friends, new Comparator<Friend>() {
            @Override
            public int compare(Friend friend, Friend friend2) {
                if(friend.isOnline() && friend2.isOnline())
                    return friend.getName().compareTo(friend2.getName());
                if(friend.isOnline())
                    return -1;
                if(friend2.isOnline())
                    return 1;
                return friend.getName().compareTo(friend2.getName());
            }
        });
        listView.setAdapter(new FriendViewAdapter(getActivity(), R.layout.friend_row, friends));
    }
}
