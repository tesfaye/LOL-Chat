package com.tesfaye.lolchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.util.List;

/**
 * Created by Abel Tesfaye on 8/26/2014.
 */
public class FriendViewAdapter extends ArrayAdapter<Friend> {
    public FriendViewAdapter(Context context, int resourceId, List<Friend> items)
    {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Friend friend = getItem(position);
        LayoutInflater mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.friend_row, parent, false);
        }
        TextView title = (TextView)convertView.findViewById(R.id.title); // title
        TextView artist = (TextView)convertView.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)convertView.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)convertView.findViewById(R.id.list_image);
        title.setText(friend.getName());
        artist.setText(friend.getStatus().toString());
        duration.setText("69");
        thumb_image.setImageDrawable(convertView.getResources().getDrawable(R.drawable.lclogo));
        return convertView;
    }
}
