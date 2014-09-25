package com.tesfayeabel.lolchat.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.LOLChatApplication;
import com.tesfayeabel.lolchat.Message;
import com.tesfayeabel.lolchat.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessageAdapter extends BaseAdapter {
    public static final int DIRECTION_INCOMING = 0;
    public static final int DIRECTION_OUTGOING = 1;
    private List<Message> messages;
    private Context context;
    private int friendProfileIcon;
    private int myProfileIcon;

    public MessageAdapter(Context con) {
        this(con, new ArrayList<Message>());
    }

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
        final Handler handler = new Handler();//update adapter every minute for in game time
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                notifyDataSetChanged();
                handler.postDelayed(this, 60 * 1000);
            }
        }, 60 * 1000);
    }

    public void setFriendProfileIcon(int friendProfileIcon) {
        this.friendProfileIcon = friendProfileIcon;
    }

    public void setMyProfileIcon(int myProfileIcon) {
        this.myProfileIcon = myProfileIcon;
    }

    //Gets called every time you update the view with an
    //incoming or outgoing message
    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    //Returns how many messages are in the list
    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //Tells your app how many possible layouts there are
    //In our case, right and left messages are our only 2 options
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //This returns either DIRECTION_INCOMING or DIRECTION_OUTGOING
    @Override
    public int getItemViewType(int i) {
        return messages.get(i).getDirection();
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Message message = getItem(i);
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            if (message.getDirection() == DIRECTION_INCOMING) {
                convertView = mInflater.inflate(R.layout.message_left, viewGroup, false);
            } else if (message.getDirection() == DIRECTION_OUTGOING) {
                convertView = mInflater.inflate(R.layout.message_right, viewGroup, false);
            }
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            holder.messageTime = (TextView) convertView.findViewById(R.id.message_time);
            holder.imageView = (ImageView) convertView.findViewById(R.id.message_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (message.getDirection() == DIRECTION_INCOMING)
            Picasso.with(context.getApplicationContext()).load(LOLChatApplication.getRiotResourceURL() + "/img/profileicon/" + friendProfileIcon + ".png").into(holder.imageView);
        if (message.getDirection() == DIRECTION_OUTGOING)
            Picasso.with(context.getApplicationContext()).load(LOLChatApplication.getRiotResourceURL() + "/img/profileicon/" + myProfileIcon + ".png").into(holder.imageView);
        holder.messageBody.setText(message.getSender() + ": " + message.getMessage());
        DateFormat dateFormat = new SimpleDateFormat("EEE hh:mmaa");
        holder.messageTime.setText(dateFormat.format(new Date(message.getTime())));
        return convertView;
    }

    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView messageBody;
        TextView messageTime;
        ImageView imageView;
    }
}
