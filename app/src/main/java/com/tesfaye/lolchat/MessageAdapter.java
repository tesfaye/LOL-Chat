package com.tesfaye.lolchat;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter{
    private List<Pair<String, Integer>> messages;
    private Context context;
    public static final int DIRECTION_INCOMING = 0;
    public static final int DIRECTION_OUTGOING = 1;

    public MessageAdapter(Context con) {
        this(con, new ArrayList<Pair<String, Integer>>());
    }
    public MessageAdapter(Context con, List list) {
        context = con;
        messages = list;
    }

    //Gets called every time you update the view with an
    //incoming or outgoing message
    public void addMessage(String message, int direction) {
        messages.add(new Pair(message, direction));
        notifyDataSetChanged();
    }

    //Returns how many messages are in the list
    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Pair<String, Integer> getItem(int i) {
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
        return getItem(i).second;
    }
    public List<Pair<String,Integer>> getMessages()
    {
        return messages;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        int direction = getItemViewType(i);
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if (direction == DIRECTION_INCOMING) {
                convertView = mInflater.inflate(R.layout.message_left, viewGroup, false);
            } else if (direction == DIRECTION_OUTGOING) {
                convertView = mInflater.inflate(R.layout.message_right, viewGroup, false);
            }
        }
        TextView view = (TextView)convertView.findViewById(R.id.text1);
        view.setText(getItem(i).first);
        return convertView;
    }
}
