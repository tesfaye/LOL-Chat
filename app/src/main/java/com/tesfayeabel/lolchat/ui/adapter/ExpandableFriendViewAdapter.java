package com.tesfayeabel.lolchat.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.LOLChatApplication;
import com.tesfayeabel.lolchat.R;
import com.tesfayeabel.lolchat.StaticFriend;
import com.tesfayeabel.lolchat.ui.ChatActivity;

import java.util.List;

/**
 * Created by Abel Tesfaye on 8/27/2014.
 */
public class ExpandableFriendViewAdapter extends BaseExpandableListAdapter {

    private List<StaticFriend> onlineFriends;
    private List<StaticFriend> offlineFriends;
    private Context context;

    public ExpandableFriendViewAdapter(Context context, List<StaticFriend> onlineFriends, List<StaticFriend> offlineFriends) {
        this.context = context;
        this.onlineFriends = onlineFriends;
        this.offlineFriends = offlineFriends;
    }

    @Override
    public StaticFriend getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).get(childPosition);
    }

    public void updateFriendStatus(StaticFriend friend) {
        List<StaticFriend> friends = getGroup(friend.isOnline() ? 0 : 1);
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).getUserId().equals(friend.getUserId())) {
                friends.set(i, friend);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void setFriendOnline(StaticFriend friend) {
        if (friend.isOnline()) {
            offlineFriends.remove(friend);
            for (int i = 0; i < onlineFriends.size(); i++) {
                if (friend.getName().toLowerCase().compareTo(onlineFriends.get(i).getName().toLowerCase()) < 0) {
                    onlineFriends.add(i, friend);
                    break;
                }
            }
        } else {
            onlineFriends.remove(friend);
            for (int i = 0; i < offlineFriends.size(); i++) {
                if (friend.getName().toLowerCase().compareTo(offlineFriends.get(i).getName().toLowerCase()) < 0) {
                    offlineFriends.add(i, friend);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0)
            return onlineFriends.size();
        return offlineFriends.size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder holder;
        final StaticFriend friend = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.friend_item, parent, false);
            holder = new ChildHolder();
            holder.friend = (TextView) convertView.findViewById(R.id.friend);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.icon = (ImageView) convertView.findViewById(R.id.list_image);
            holder.statusCircle = convertView.findViewById(R.id.statusCircle);
            holder.button = (Button) convertView.findViewById(R.id.button);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        GradientDrawable shapeDrawable = (GradientDrawable) holder.statusCircle.getBackground();
        holder.friend.setText(friend.getName());
        if (groupPosition == 0) {
            holder.status.setVisibility(View.VISIBLE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.statusCircle.setVisibility(View.VISIBLE);
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("friend", friend.getName());
                    context.startActivity(intent);
                }
            });
            holder.status.setText(friend.getFullStatus());
            int iconId = friend.getStatus().getProfileIconId();
            if (iconId == -1)
                iconId = 1;
            switch (friend.getChatMode()) {
                case AVAILABLE:
                    shapeDrawable.setColor(Color.GREEN);
                    holder.status.setTextColor(Color.GREEN);
                    break;
                case BUSY:
                    shapeDrawable.setColor(Color.rgb(252, 209, 33));
                    holder.status.setTextColor(Color.rgb(252, 209, 33));
                    break;
                case AWAY:
                    shapeDrawable.setColor(Color.RED);
                    holder.status.setTextColor(Color.RED);
                    break;
            }
            Picasso.with(context.getApplicationContext()).load(LOLChatApplication.getRiotResourceURL() + "/img/profileicon/" + iconId + ".png").into(holder.icon);
        } else {
            holder.status.setVisibility(View.GONE);
            holder.icon.setVisibility(View.GONE);
            holder.statusCircle.setVisibility(View.GONE);
            holder.button.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public List<StaticFriend> getGroup(int groupPosition) {
        if (groupPosition == 0)
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
        GroupHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
            holder = new GroupHolder();
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        String text = "";
        if (groupPosition == 0) {
            text = "Online (" + onlineFriends.size() + ")";
        }
        if (groupPosition == 1) {
            text = "Offline (" + offlineFriends.size() + ")";
        }
        holder.textView.setText(text);
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

    private class ChildHolder {
        TextView friend;
        TextView status;
        ImageView icon;
        View statusCircle;
        Button button;
    }

    private class GroupHolder {
        TextView textView;
    }

}
