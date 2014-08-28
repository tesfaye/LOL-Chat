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
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Abel Tesfaye on 8/27/2014.
 */
public class ExpandableFriendViewAdapter extends BaseExpandableListAdapter {

    public class ViewHolder
    {
        TextView title;
        TextView artist;
        ImageView thumb_image;
        View view;
        Button button;
    }

    private List<Friend> onlineFriends;
    private List<Friend> offLineFriends;
    private Context context;

    public ExpandableFriendViewAdapter(Context context, List<Friend> onlineFriends, List<Friend> offLineFriends) {
        this.context = context;
        this.onlineFriends = onlineFriends;
        this.offLineFriends = offLineFriends;
    }

    @Override
    public Friend getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition == 0)
            return onlineFriends.size();
        return offLineFriends.size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        Friend friend = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.friend_row, null);
            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.artist = (TextView)convertView.findViewById(R.id.artist);
            holder.thumb_image = (ImageView)convertView.findViewById(R.id.list_image);
            holder.view = convertView.findViewById(R.id.statusCircle);
            holder.button = (Button) convertView.findViewById(R.id.button);
            convertView.setTag(holder);
        } else {
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
                            ((Activity) context).runOnUiThread(new Runnable() {
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

    @Override
    public List<Friend> getGroup(int groupPosition) {
        if(groupPosition == 0)
            return onlineFriends;
        return offLineFriends;
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        String text = "";
        if(groupPosition == 0)
        {
            text = "Online";
        }
        if(groupPosition == 1)
        {
            text = "Offline";
        }
        textView.setText(text);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
