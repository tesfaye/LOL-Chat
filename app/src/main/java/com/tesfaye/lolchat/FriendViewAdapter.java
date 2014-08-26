package com.tesfaye.lolchat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.io.InputStream;
import java.net.URL;
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
        final ImageView thumb_image=(ImageView)convertView.findViewById(R.id.list_image);
        title.setText(friend.getName());
        LolStatus.GameStatus gameStatus = friend.getStatus().getGameStatus();
        String status;
        int iconId;
        if(gameStatus == null)
            status = friend.getStatus().getStatusMessage() + "\n" + "Online";
        else
            status = friend.getStatus().getStatusMessage() + "\n" + gameStatus.internal();
        iconId = friend.getStatus().getProfileIconId();
        if(iconId == -1)
            iconId = 1;
        final int profileIcon = iconId;
        artist.setText(status);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{//TODO: CACHE IMAGES
                    final Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://ddragon.leagueoflegends.com/cdn/4.14.2/img/profileicon/"+ profileIcon + ".png").getContent());
                    if(bitmap != null)
                    {
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                thumb_image.setImageBitmap(bitmap);
                            }
                        });
                    }}
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
        }).start();
        return convertView;
    }
}
