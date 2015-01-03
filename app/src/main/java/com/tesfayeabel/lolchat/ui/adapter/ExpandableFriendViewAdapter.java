/*
 * LOL-Chat
 * Copyright (C) 2014  Abel Tesfaye
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tesfayeabel.lolchat.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.LOLChatApplication;
import com.tesfayeabel.lolchat.R;
import com.tesfayeabel.lolchat.data.StaticFriend;
import com.tesfayeabel.lolchat.ui.ChatActivity;

import java.util.ArrayList;

public class ExpandableFriendViewAdapter extends BaseExpandableListAdapter implements Filterable{

    private ArrayList<StaticFriend> onlineFriends;
    private ArrayList<StaticFriend> unfilteredOnlineFriends;
    private ArrayList<StaticFriend> offlineFriends;
    private ArrayList<StaticFriend> unfilteredOfflineFriends;
    private FriendViewFilter mFilter;
    private Context context;

    public ExpandableFriendViewAdapter(Context context, ArrayList<StaticFriend> onlineFriends, ArrayList<StaticFriend> offlineFriends) {
        this.context = context;
        this.unfilteredOnlineFriends = this.onlineFriends = onlineFriends;
        this.unfilteredOfflineFriends = this.offlineFriends = offlineFriends;
        final Handler handler = new Handler();//update adapter every minute for in game time
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                notifyDataSetChanged();
                handler.postDelayed(this, 60 * 1000);
            }
        }, 60 * 1000);
    }

    @Override
    public StaticFriend getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).get(childPosition);
    }

    /**
     * Removes and adds a StaticFriend and updates ui
     *
     * @param friend whose status is to be updated
     */
    public void updateFriendStatus(StaticFriend friend) {
        ArrayList<StaticFriend> friends = getGroup(friend.isOnline() ? 0 : 1);
        friends.remove(friend);
        for (int i = 0; i < friends.size(); i++) {
            if (friend.compareTo(friends.get(i)) < 0) {
                friends.add(i, friend);
                break;
            }
        }
        if (!friends.contains(friend)) {
            friends.add(friend);//add to end of list
        }
        notifyDataSetChanged();
    }

    /**
     * Makes a StaticFriend either online or offline and updates ui
     *
     * @param friend to become online or offline
     */
    public void setFriendOnline(StaticFriend friend) {
        if (friend.isOnline()) {
            offlineFriends.remove(friend);
            for (int i = 0; i < onlineFriends.size(); i++) {
                if (friend.compareTo(onlineFriends.get(i)) < 0) {
                    onlineFriends.add(i, friend);
                    break;
                }
            }
            if (!onlineFriends.contains(friend))
                onlineFriends.add(friend);//add to end of list
        } else {
            onlineFriends.remove(friend);
            for (int i = 0; i < offlineFriends.size(); i++) {
                if (friend.compareTo(offlineFriends.get(i)) < 0) {
                    offlineFriends.add(i, friend);
                    break;
                }
            }
            if (!offlineFriends.contains(friend))
                offlineFriends.add(friend);//add to end of list
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
            int iconId = friend.getProfileIconId();
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
            Picasso.with(context.getApplicationContext()).load(LOLChatApplication.getProfileIconURL(iconId)).into(holder.icon);
        } else {
            holder.status.setVisibility(View.GONE);
            holder.icon.setVisibility(View.GONE);
            holder.statusCircle.setVisibility(View.GONE);
            holder.button.setVisibility(View.GONE);
        }
        return convertView;
    }

    /**
     * http://stackoverflow.com/questions/5713585/how-to-preserve-scroll-position-in-an-expandablelistview
     *
     * @param groupId
     * @param childId
     * @return positive child id
     */
    @Override
    public long getCombinedChildId(long groupId, long childId) {
        long or = 0x7000000000000000L;
        long group = (groupId & 0x7FFFFFFF) << 32;
        long child = childId & 0xFFFFFFFF;
        return or | group | child;
    }

    @Override
    public ArrayList<StaticFriend> getGroup(int groupPosition) {
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

    @Override
    public Filter getFilter() {
        if(mFilter == null) {
            mFilter = new FriendViewFilter();
        }
        return mFilter;
    }

    private class FriendViewFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<StaticFriend> filteredOnline = new ArrayList<StaticFriend>();
                ArrayList<StaticFriend> filteredOffline = new ArrayList<StaticFriend>();
                for (StaticFriend friend : unfilteredOnlineFriends) {
                    if (friend.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredOnline.add(friend);
                    }
                }
                for (StaticFriend friend : unfilteredOfflineFriends) {
                    if (friend.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredOffline.add(friend);
                    }
                }
                results.count = -1;//unused
                results.values = new ArrayList[]{filteredOnline, filteredOffline};
            } else {
                results.count = -1;//unused
                results.values = new ArrayList[]{unfilteredOnlineFriends, unfilteredOfflineFriends};
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<StaticFriend>[] friends = (ArrayList<StaticFriend>[]) results.values;
            onlineFriends = friends[0];
            offlineFriends = friends[1];
            notifyDataSetChanged();
        }
    }
}
