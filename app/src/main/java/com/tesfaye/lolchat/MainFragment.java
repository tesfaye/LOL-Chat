package com.tesfaye.lolchat;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.github.theholywaffle.lolchatapi.LolChat;

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
        listView.setAdapter(new ExpandableFriendViewAdapter(getActivity(), chat.getOnlineFriends(), chat.getOfflineFriends()));
        listView.expandGroup(0);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("listView", listView.onSaveInstanceState());
    }
}
