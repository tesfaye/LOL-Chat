package com.tesfaye.lolchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    private List<Friend> offlineFriends;
    private Context context;

    public ExpandableFriendViewAdapter(Context context, List<Friend> onlineFriends, List<Friend> offlineFriends) {
        this.context = context;
        this.onlineFriends = onlineFriends;
        this.offlineFriends = offlineFriends;
    }
    @Override
    public Friend getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).get(childPosition);
    }
    public void setFriendStatus(Friend friend, boolean online)
    {
        if(online)
        {
            for(int i =0; i<onlineFriends.size(); i++)
            {
                Friend f = onlineFriends.get(i);
                if(f.getUserId().equals(friend.getUserId()))
                    f.setStatus(friend.getStatus());
            }
        }else
        {
            for(int i =0; i<offlineFriends.size(); i++)
            {
                Friend f = offlineFriends.get(i);
                if(f.getUserId().equals(friend.getUserId()))
                    f.setStatus(friend.getStatus());
            }
        }
        notifyDataSetChanged();
    }
    public void setFriendOnline(Friend friend, boolean online)
    {
        if(online)
        {
            for(int i =0; i<offlineFriends.size(); i++)
            {
                Friend f = offlineFriends.get(i);
                if(f.getUserId().equals(friend.getUserId()))
                    offlineFriends.remove(f);
            }
            onlineFriends.add(friend);
        }else
        {
            for(int i =0; i<onlineFriends.size(); i++)
            {
                Friend f = onlineFriends.get(i);
                if(f.getUserId().equals(friend.getUserId()))
                    onlineFriends.remove(f);
            }
            offlineFriends.add(friend);
        }
        notifyDataSetChanged();
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition == 0)
            return onlineFriends.size();
        return offlineFriends.size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Friend friend = getChild(groupPosition, childPosition);
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
        if(groupPosition == 0) {
            holder.artist.setVisibility(View.VISIBLE);
            holder.thumb_image.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("friend", friend.getName());
                    context.startActivity(intent);
                }
            });
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
                    try {
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
        return offlineFriends;
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
        convertView = mInflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
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
