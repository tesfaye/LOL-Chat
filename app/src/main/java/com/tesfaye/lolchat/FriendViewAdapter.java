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
import android.widget.Button;
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
        View view;
        Button button;
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
            holder.view = convertView.findViewById(R.id.statusCircle);
            holder.button = (Button) convertView.findViewById(R.id.button);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        GradientDrawable shapeDrawable = (GradientDrawable) holder.view.getBackground();
        holder.title.setText(friend.getName());
        if(friend.isOnline()) {
            holder.artist.setVisibility(View.VISIBLE);
            holder.thumb_image.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
            holder.button.setVisibility(View.VISIBLE);
            LolStatus.GameStatus gameStatus = friend.getStatus().getGameStatus();
            String status;
            if (gameStatus == null)
                status = friend.getStatus().getStatusMessage() + "\n" + "Online";
            else
                status = friend.getStatus().getStatusMessage() + "\n" + gameStatus.internal();
            holder.artist.setText(status);
            int iconId = friend.getStatus().getProfileIconId();
            if (iconId == -1)
                iconId = 1;
            switch (friend.getChatMode()) {
                case AVAILABLE:
                    shapeDrawable.setColor(Color.GREEN);
                    break;
                case BUSY:
                    shapeDrawable.setColor(Color.rgb(252, 209, 33));
                    break;
                case AWAY:
                    shapeDrawable.setColor(Color.RED);
                    break;
            }
            final int profileIcon = iconId;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {//TODO: CACHE IMAGES
                        final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL("http://ddragon.leagueoflegends.com/cdn/4.14.2/img/profileicon/" + profileIcon + ".png").getContent());
                        if (bitmap != null) {
                            ((Activity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.thumb_image.setImageBitmap(bitmap);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else
        {
            holder.artist.setVisibility(View.GONE);
            holder.thumb_image.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
            holder.button.setVisibility(View.GONE);
        }
        return convertView;
    }
}
