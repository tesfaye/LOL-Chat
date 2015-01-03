package com.tesfayeabel.lolchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.listeners.FriendListener;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.tesfayeabel.lolchat.R;
import com.tesfayeabel.lolchat.data.StaticFriend;
import com.tesfayeabel.lolchat.ui.adapter.ExpandableFriendViewAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class MainFragment extends LOLChatFragment {
    private ExpandableListView listView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lolchat_main, container, false);
        listView = (ExpandableListView) view.findViewById(R.id.listView);
        if (savedInstanceState != null) {
            ArrayList<StaticFriend> online = savedInstanceState.getParcelableArrayList("onlineFriends");
            ArrayList<StaticFriend> offline = savedInstanceState.getParcelableArrayList("offlineFriends");
            listView.setAdapter(new ExpandableFriendViewAdapter(getActivity(), online, offline));
            listView.onRestoreInstanceState(savedInstanceState.getParcelable("listView"));
        }
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                ExpandableFriendViewAdapter adapter = (ExpandableFriendViewAdapter) listView.getExpandableListAdapter();
                intent.putExtra("player", adapter.getChild(groupPosition, childPosition).getName());
                startActivity(intent);
                return true;
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    public void onChatConnected(final LolChat chat) {
        if (listView.getExpandableListAdapter() == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<StaticFriend> online = new ArrayList<StaticFriend>();
                    final ArrayList<StaticFriend> offline = new ArrayList<StaticFriend>();
                    for (Friend friend : chat.getOnlineFriends()) {
                        online.add(new StaticFriend(friend));
                    }
                    for (Friend friend : chat.getOfflineFriends()) {
                        offline.add(new StaticFriend(friend));
                    }
                    Collections.sort(online);
                    Collections.sort(offline);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(new ExpandableFriendViewAdapter(getActivity(), online, offline));
                            listView.expandGroup(0);
                        }
                    });
                }
            }).start();
        }
        chat.addFriendListener(new FriendListener() {

            private void update(final Friend friend) {
                final ExpandableFriendViewAdapter adapter = (ExpandableFriendViewAdapter) listView.getExpandableListAdapter();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.updateFriendStatus(new StaticFriend(friend));
                        }
                    });
                }
            }

            @Override
            public void onFriendAvailable(final Friend friend) {
                update(friend);
            }

            @Override
            public void onFriendAway(final Friend friend) {
                update(friend);
            }

            @Override
            public void onFriendBusy(final Friend friend) {
                update(friend);
            }

            @Override
            public void onFriendJoin(final Friend friend) {
                final ExpandableFriendViewAdapter adapter = (ExpandableFriendViewAdapter) listView.getExpandableListAdapter();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setFriendOnline(new StaticFriend(friend));
                        }
                    });
                }
            }

            @Override
            public void onFriendLeave(final Friend friend) {
                final ExpandableFriendViewAdapter adapter = (ExpandableFriendViewAdapter) listView.getExpandableListAdapter();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setFriendOnline(new StaticFriend(friend));
                        }
                    });
                }
            }

            @Override
            public void onFriendStatusChange(final Friend friend) {
                update(friend);
            }

            @Override
            public void onNewFriend(Friend friend) {

            }

            @Override
            public void onRemoveFriend(String userId) {

            }
        });
        while (chat.getFriendListeners().size() > 1) {
            chat.getFriendListeners().remove(0);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ExpandableFriendViewAdapter adapter = (ExpandableFriendViewAdapter) listView.getExpandableListAdapter();
                adapter.getFilter().filter(s);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("listView", listView.onSaveInstanceState());
        ExpandableFriendViewAdapter adapter = (ExpandableFriendViewAdapter) listView.getExpandableListAdapter();
        savedInstanceState.putParcelableArrayList("onlineFriends", adapter.getGroup(0));
        savedInstanceState.putParcelableArrayList("offlineFriends", adapter.getGroup(1));
    }
}
