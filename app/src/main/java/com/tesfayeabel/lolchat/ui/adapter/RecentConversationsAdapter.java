package com.tesfayeabel.lolchat.ui.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.LOLChatApplication;
import com.tesfayeabel.lolchat.R;
import com.tesfayeabel.lolchat.data.RecentConversation;

import java.util.List;

public class RecentConversationsAdapter extends ArrayAdapter<RecentConversation> {

    private int resource;
    private List<RecentConversation> recentConversations;

    public RecentConversationsAdapter(Context context, int resource, List<RecentConversation> recentConversations) {
        super(context, resource, recentConversations);
        this.resource = resource;
        this.recentConversations = recentConversations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.conversationImage = (ImageView) convertView.findViewById(R.id.conversation_image);
            holder.conversationName = (TextView) convertView.findViewById(R.id.conversation_name);
            holder.lastUpdate = (TextView) convertView.findViewById(R.id.conversation_lastupdate);
            holder.lastMessage = (TextView) convertView.findViewById(R.id.conversation_lastmsg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecentConversation conversation = getItem(position);
        Picasso.with(getContext().getApplicationContext()).load(LOLChatApplication.getProfileIconURL(conversation.getProfileIconId())).into(holder.conversationImage);
        holder.conversationName.setText(conversation.getName());
        holder.lastUpdate.setText(DateUtils.getRelativeTimeSpanString(conversation.getLastUpdate()));
        holder.lastMessage.setText(conversation.getLastMessage());
        return convertView;
    }

    public List<RecentConversation> getRecentConversations() {
        return recentConversations;
    }

    public void updateConversation(String name, String lastMessage, long lastUpdate) {
        RecentConversation recentConversation = null;
        for (RecentConversation conversation : recentConversations) {
            if (name.equals(conversation.getName()))
                recentConversation = conversation;
        }
        if (recentConversation == null)
            recentConversation = new RecentConversation(1, name);
        recentConversations.remove(recentConversation);
        recentConversation.setLastMessage(lastMessage);
        recentConversation.setLastUpdate(lastUpdate);
        recentConversations.add(0, recentConversation);
        notifyDataSetChanged();
    }

    public void deleteConversation(String name) {
        RecentConversation recentConversation = null;
        for (RecentConversation conversation : recentConversations) {
            if (name.equals(conversation.getName()))
                recentConversation = conversation;
        }
        recentConversations.remove(recentConversation);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView conversationImage;
        TextView conversationName;
        TextView lastUpdate;
        TextView lastMessage;
    }

}
