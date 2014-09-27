package com.tesfayeabel.lolchat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.theholywaffle.lolchatapi.LolChat;
import com.tesfayeabel.lolchat.Message;
import com.tesfayeabel.lolchat.R;
import com.tesfayeabel.lolchat.ui.adapter.MessageAdapter;
import com.tesfayeabel.lolchat.ui.adapter.RecentConversationsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abel Tesfaye on 9/21/2014.
 */
public class ConversationsFragment extends LOLChatFragment {

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lolchat_conversations, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        if (savedInstanceState != null) {
            listView.setAdapter(new RecentConversationsAdapter(getActivity(), R.layout.message_conversation_item, (ArrayList) savedInstanceState.getParcelableArrayList("conversations")));
            listView.onRestoreInstanceState(savedInstanceState.getParcelable("listView"));
        } else {
            ArrayList<RecentConversation> test = new ArrayList<RecentConversation>();
            test.add(new RecentConversation(1, "Test", 100900000, "Test message"));
            test.add(new RecentConversation(2, "Test1", 10090000, "Test message 1"));
            test.add(new RecentConversation(3, "Test2", 100900000, "Test message 2"));
            listView.setAdapter(new RecentConversationsAdapter(getActivity(), R.layout.message_conversation_item, test));
        }
        return view;
    }

    @Override
    public void onChatConnected(final LolChat chat) {

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("listView", listView.onSaveInstanceState());
        savedInstanceState.putParcelableArrayList("conversations", (ArrayList<RecentConversation>) ((RecentConversationsAdapter) listView.getAdapter()).getRecentConversations());
    }
}
