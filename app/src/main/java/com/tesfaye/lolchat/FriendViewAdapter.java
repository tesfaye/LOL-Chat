package com.tesfaye.lolchat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
    public class ViewHolder
    {
        TextView title;
        TextView artist;
        ImageView thumb_image;
        GradientDrawable shapeDrawable;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        Friend friend = getItem(position);
        LayoutInflater mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.friend_row, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.artist = (TextView)convertView.findViewById(R.id.artist);
            holder.thumb_image = (ImageView)convertView.findViewById(R.id.list_image);
            holder.shapeDrawable = (GradientDrawable)convertView.findViewById(R.id.statusCircle).getBackground();
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(friend.getName());
        LolStatus.GameStatus gameStatus = friend.getStatus().getGameStatus();
        String status;
        if(gameStatus == null)
            status = friend.getStatus().getStatusMessage() + "\n" + "Online";
        else
            status = friend.getStatus().getStatusMessage() + "\n" + gameStatus.internal();
        holder.artist.setText(status);
        int iconId = friend.getStatus().getProfileIconId();
        if(iconId == -1)
            iconId = 1;
        switch(friend.getChatMode())
        {
            case AVAILABLE:
                holder.shapeDrawable.setColor(Color.GREEN);
                break;
            case BUSY:
                holder.shapeDrawable.setColor(Color.rgb(252, 209, 33));
                break;
            case AWAY:
                holder.shapeDrawable.setColor(Color.RED);
                break;
        }
        final int profileIcon = iconId;
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
                                holder.thumb_image.setImageBitmap(bitmap);
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
