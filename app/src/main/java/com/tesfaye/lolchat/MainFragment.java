package com.tesfaye.lolchat;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.listeners.FriendListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainFragment extends LOLChatFragment
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
    public void onChatConnected(final LolChat chat) {
        List<Friend> online = chat.getOnlineFriends();
        List<Friend> offline = chat.getOfflineFriends();
        Comparator comparator = new Comparator<Friend>() {
            @Override
            public int compare(Friend friend, Friend friend2) {
                return friend.getName().toLowerCase().compareTo(friend2.getName().toLowerCase());
            }
        };
        Collections.sort(online, comparator);
        Collections.sort(offline, comparator);
        listView.setAdapter(new ExpandableFriendViewAdapter(getActivity(), online, offline));
        chat.addFriendListener(new FriendListener() {
            @Override
            public void onFriendAvailable(Friend friend) {

            }

            @Override
            public void onFriendAway(Friend friend) {

            }

            @Override
            public void onFriendBusy(Friend friend) {

            }

            @Override
            public void onFriendJoin(final Friend friend) {
                final ExpandableFriendViewAdapter adapter = (ExpandableFriendViewAdapter)listView.getExpandableListAdapter();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setFriendOnline(friend, true);
                    }
                });
            }

            @Override
            public void onFriendLeave(final Friend friend) {
                final ExpandableFriendViewAdapter adapter = (ExpandableFriendViewAdapter)listView.getExpandableListAdapter();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setFriendOnline(friend, false);
                    }
                });
            }

            @Override
            public void onFriendStatusChange(final Friend friend) {
                final ExpandableFriendViewAdapter adapter = (ExpandableFriendViewAdapter)listView.getExpandableListAdapter();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateFriendStatus(friend);
                    }
                });
            }

            @Override
            public void onNewFriend(Friend friend) {

            }

            @Override
            public void onRemoveFriend(String userId) {

            }
        });
        listView.expandGroup(0);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("listView", listView.onSaveInstanceState());
    }
}
