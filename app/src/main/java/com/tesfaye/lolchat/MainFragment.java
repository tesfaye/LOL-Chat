package com.tesfaye.lolchat;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.github.theholywaffle.lolchatapi.LolChat;
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
        enableHttpResponseCache();
        return view;
    }
    public void onChatConnected(final LolChat chat) {
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
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)  {
                if(groupPosition == 0)
                {
                    Friend selected = ((ExpandableFriendViewAdapter)parent.getExpandableListAdapter()).getChild(groupPosition, childPosition);
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("friend", selected.getName());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        listView.expandGroup(0);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("listView", listView.onSaveInstanceState());
    }
    private void enableHttpResponseCache() {
        try {
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            File httpCacheDir = new File(getActivity().getCacheDir(), "http");
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
            //cache not supported
        }
    }
}
