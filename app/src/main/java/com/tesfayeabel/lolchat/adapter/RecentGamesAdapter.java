package com.tesfayeabel.lolchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tesfayeabel.lolchat.R;

import java.util.List;

import jriot.objects.Game;

public class RecentGamesAdapter extends BaseAdapter {

    private List<Game> games;
    private Context context;

    public RecentGamesAdapter(Context context, List<Game> list) {
        this.context = context;
        games = list;
    }

    //Returns how many messages are in the list
    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Game getItem(int i) {
        return games.get(i);
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
        return 0;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.message_left, viewGroup, false);
        }
        Game game = getItem(i);
        return convertView;
    }
}
