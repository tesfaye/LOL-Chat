package com.tesfayeabel.lolchat.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.tesfayeabel.lolchat.data.Message;
import com.tesfayeabel.lolchat.R;
import com.tesfayeabel.lolchat.data.RecentConversation;
import com.tesfayeabel.lolchat.ui.adapter.RecentConversationsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by Abel Tesfaye on 9/21/2014.
 */
public class ConversationsFragment extends LOLChatFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ListView listView;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lolchat_conversations, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        sharedPreferences = getActivity().getSharedPreferences("messageHistory", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if (savedInstanceState != null) {
            listView.setAdapter(new RecentConversationsAdapter(getActivity(), R.layout.message_conversation_item, (ArrayList) savedInstanceState.getParcelableArrayList("conversations")));
            listView.onRestoreInstanceState(savedInstanceState.getParcelable("listView"));
        } else {
            Map<String, ?> keys = sharedPreferences.getAll();
            ArrayList<RecentConversation> conversations = new ArrayList<RecentConversation>();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                String[] messages = entry.getValue().toString().split("\n");
                Message last = new Message(messages[messages.length - 1]);
                conversations.add(new RecentConversation(1, entry.getKey(), last.getSender() + ": " + last.getMessage(), last.getTime()));
            }
            Collections.sort(conversations, new Comparator<RecentConversation>() {
                @Override
                public int compare(RecentConversation recentConversation, RecentConversation recentConversation2) {
                    return (int) (recentConversation2.getLastUpdate() - recentConversation.getLastUpdate());
                }
            });
            listView.setAdapter(new RecentConversationsAdapter(getActivity(), R.layout.message_conversation_item, conversations));
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecentConversation conversation = (RecentConversation) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("friend", conversation.getName());
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onChatConnected(final LolChat chat) {
        RecentConversationsAdapter adapter = (RecentConversationsAdapter) listView.getAdapter();
        for(Friend friend: chat.getOnlineFriends()) {
            for(RecentConversation conversation : adapter.getRecentConversations()) {
                if(friend.getName().equals(conversation.getName())) {
                    conversation.setProfileIconId(friend.getStatus().getProfileIconId());
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("listView", listView.onSaveInstanceState());
        savedInstanceState.putParcelableArrayList("conversations", (ArrayList<RecentConversation>) ((RecentConversationsAdapter) listView.getAdapter()).getRecentConversations());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        RecentConversationsAdapter adapter = (RecentConversationsAdapter) listView.getAdapter();
        String message = preferences.getString(key, null);
        if (message != null) {
            String[] messages = message.split("\n");
            Message last = new Message(messages[messages.length - 1]);
            adapter.updateConversation(key, last.getSender() + ": " + last.getMessage(), last.getTime());
        } else {
            adapter.deleteConversation(key);
        }
    }
}
