package com.tesfaye.lolchat;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainFragment extends Fragment
{
    private ExpandableListView listView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_lolchat_main, container, false);
        listView  = (ExpandableListView) view.findViewById(R.id.listView);
        if(savedInstanceState != null) {
            listView.onRestoreInstanceState(savedInstanceState.getParcelable("listView"));
        }
        return view;
    }
    public void onChatConnected(LolChat chat) {
        List<Friend> online = chat.getOnlineFriends();
        List<Friend> offline = chat.getOfflineFriends();
        Comparator comparator = new Comparator<Friend>() {
            @Override
            public int compare(Friend friend, Friend friend2) {
                return friend.getName().compareTo(friend2.getName());
            }
        };
        Collections.sort(online, comparator);
        Collections.sort(offline, comparator);
        listView.setAdapter(new ExpandableFriendViewAdapter(getActivity(), online, offline));
        listView.expandGroup(0);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("listView", listView.onSaveInstanceState());
    }
}
