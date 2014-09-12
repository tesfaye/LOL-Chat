package com.tesfayeabel.lolchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.LOLChatApplication;
import com.tesfayeabel.lolchat.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import jriot.objects.Game;
import jriot.objects.RawStats;

public class RecentGamesAdapter extends BaseExpandableListAdapter {

    private List<Game> games;
    private Context context;

    public RecentGamesAdapter(Context context, List<Game> list) {
        this.context = context;
        games = list;
    }

    @Override
    public int getGroupCount() {
        return games.size();
    }

    @Override
    public Game getChild(int group, int child) {
        return games.get(group);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public long getChildId(int group, int child) {
        return child;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.recent_game_item, parent, false);
            holder = new GroupHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.gameavatar);
            holder.outcome = (TextView) convertView.findViewById(R.id.labelstatus);
            holder.type = (TextView) convertView.findViewById(R.id.labeltype);
            holder.map = (TextView) convertView.findViewById(R.id.labellocation);
            holder.date = (TextView) convertView.findViewById(R.id.labeldate);
            holder.ip = (TextView) convertView.findViewById(R.id.labelip);
            holder.assists = (TextView) convertView.findViewById(R.id.handval);
            holder.deaths = (TextView) convertView.findViewById(R.id.skullval);
            holder.kills = (TextView) convertView.findViewById(R.id.swordval);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        final Game game = getGroup(groupPosition);
        RawStats stats = game.getStats();
        holder.outcome.setText(stats.getWin() ? "Victory" : "Defeat");
        holder.type.setText(game.getGameMode());
        Picasso.with(context.getApplicationContext()).load(LOLChatApplication.getRiotResourceURL() + "/img/champion/" + game.getChampionName().replace(" ", "") + ".png").into(holder.avatar);
        holder.map.setText(LOLChatApplication.getMapName(game.getMapId()));
        holder.ip.setText(String.valueOf(game.getIpEarned()) + " IP");
        holder.date.setText(DateFormat.getDateInstance().format(new Date(game.getCreateDate())));
        holder.kills.setText(String.valueOf(stats.getChampionsKilled()));
        holder.deaths.setText(String.valueOf(stats.getNumDeaths()));
        holder.assists.setText(String.valueOf(stats.getAssists()));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.recent_game_item, parent, false);
            holder = new ChildHolder();

            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        Game game = getChild(groupPosition, childPosition);
        return convertView;
    }

    @Override
    public Game getGroup(int groupPosition) {
        return games.get(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class GroupHolder {
        ImageView avatar;
        TextView outcome;
        TextView type;
        TextView map;
        TextView date;
        TextView ip;
        TextView assists;
        TextView deaths;
        TextView kills;
    }

    public class ChildHolder {

    }
}
