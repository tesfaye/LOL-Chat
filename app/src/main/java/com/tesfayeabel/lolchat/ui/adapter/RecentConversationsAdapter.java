package com.tesfayeabel.lolchat.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.LOLChatApplication;
import com.tesfayeabel.lolchat.R;
import com.tesfayeabel.lolchat.ui.RecentConversation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecentConversationsAdapter extends ArrayAdapter<RecentConversation> {

    private int resource;

    public RecentConversationsAdapter(Context context, int resource, List<RecentConversation> recentConversations) {
        super(context, resource, recentConversations);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
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
        Picasso.with(getContext().getApplicationContext()).load(LOLChatApplication.getRiotResourceURL() + "/img/profileicon/" + conversation.getProfileIconId()+ ".png").into(holder.conversationImage);
        holder.conversationName.setText(conversation.getName());
        DateFormat dateFormat = new SimpleDateFormat("EEE hh:mmaa");
        holder.lastUpdate.setText(dateFormat.format(new Date(conversation.getLastUpdate())));
        holder.lastMessage.setText(conversation.getLastMessage());
        return convertView;
    }

    private class ViewHolder {
        ImageView conversationImage;
        TextView conversationName;
        TextView lastUpdate;
        TextView lastMessage;
    }

}
