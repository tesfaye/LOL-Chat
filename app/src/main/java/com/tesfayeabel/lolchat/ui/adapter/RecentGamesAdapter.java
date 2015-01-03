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
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tesfayeabel.lolchat.LOLChatApplication;
import com.tesfayeabel.lolchat.LOLUtils;
import com.tesfayeabel.lolchat.R;

import java.util.Date;
import java.util.List;

import jriot.objects.Game;
import jriot.objects.Player;
import jriot.objects.RawStats;

public class RecentGamesAdapter extends BaseExpandableListAdapter {

    private List<Game> recentGames;
    private Context context;

    public RecentGamesAdapter(Context context, List<Game> recentGames) {
        this.context = context;
        this.recentGames = recentGames;
    }

    @Override
    public int getGroupCount() {
        return recentGames.size();
    }

    @Override
    public Game getChild(int group, int child) {
        return recentGames.get(group);
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
        holder.outcome.setText((stats.getWin() ? "Victory" : "Defeat") + " (" + LOLUtils.getGameSubType(game.getSubType()) + ")");
        holder.type.setText(LOLUtils.getGameMode(game.getGameMode()));
        holder.avatar.setImageResource(LOLChatApplication.getChampionResourceFromId(game.getChampionId()));
        holder.map.setText(LOLUtils.getMapName(game.getMapId()));
        holder.ip.setText("+" + String.valueOf(game.getIpEarned()) + " IP");
        holder.date.setText(DateFormat.getDateFormat(context).format(new Date(game.getCreateDate())));
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
            convertView = mInflater.inflate(R.layout.recent_game_detail_item, parent, false);
            holder = new ChildHolder();
            holder.summonerSpell1 = (ImageView) convertView.findViewById(R.id.summonerSpell1);
            holder.summonerSpell2 = (ImageView) convertView.findViewById(R.id.summonerSpell2);
            holder.item1 = (ImageView) convertView.findViewById(R.id.summonerItem1);
            holder.item2 = (ImageView) convertView.findViewById(R.id.summonerItem2);
            holder.item3 = (ImageView) convertView.findViewById(R.id.summonerItem3);
            holder.item4 = (ImageView) convertView.findViewById(R.id.summonerItem4);
            holder.item5 = (ImageView) convertView.findViewById(R.id.summonerItem5);
            holder.item6 = (ImageView) convertView.findViewById(R.id.summonerItem6);
            holder.item7 = (ImageView) convertView.findViewById(R.id.trinket);
            holder.champ = new ImageView[10];
            holder.champ[0] = (ImageView) convertView.findViewById(R.id.champ1);
            holder.champ[1] = (ImageView) convertView.findViewById(R.id.champ2);
            holder.champ[2] = (ImageView) convertView.findViewById(R.id.champ3);
            holder.champ[3] = (ImageView) convertView.findViewById(R.id.champ4);
            holder.champ[4] = (ImageView) convertView.findViewById(R.id.champ5);
            holder.champ[5] = (ImageView) convertView.findViewById(R.id.champ6);
            holder.champ[6] = (ImageView) convertView.findViewById(R.id.champ7);
            holder.champ[7] = (ImageView) convertView.findViewById(R.id.champ8);
            holder.champ[8] = (ImageView) convertView.findViewById(R.id.champ9);
            holder.champ[9] = (ImageView) convertView.findViewById(R.id.champ10);
            holder.sum = new TextView[10];
            holder.sum[0] = (TextView) convertView.findViewById(R.id.sum1);
            holder.sum[1] = (TextView) convertView.findViewById(R.id.sum2);
            holder.sum[2] = (TextView) convertView.findViewById(R.id.sum3);
            holder.sum[3] = (TextView) convertView.findViewById(R.id.sum4);
            holder.sum[4] = (TextView) convertView.findViewById(R.id.sum5);
            holder.sum[5] = (TextView) convertView.findViewById(R.id.sum6);
            holder.sum[6] = (TextView) convertView.findViewById(R.id.sum7);
            holder.sum[7] = (TextView) convertView.findViewById(R.id.sum8);
            holder.sum[8] = (TextView) convertView.findViewById(R.id.sum9);
            holder.sum[9] = (TextView) convertView.findViewById(R.id.sum10);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        Game game = getChild(groupPosition, childPosition);
        RawStats stats = game.getStats();

        holder.summonerSpell1.setImageResource(LOLChatApplication.getSpellResourceFromId(game.getSpell1()));
        holder.summonerSpell2.setImageResource(LOLChatApplication.getSpellResourceFromId(game.getSpell2()));

        holder.item1.setImageResource(LOLChatApplication.getItemResourceFromId(stats.getItem0()));
        holder.item2.setImageResource(LOLChatApplication.getItemResourceFromId(stats.getItem1()));
        holder.item3.setImageResource(LOLChatApplication.getItemResourceFromId(stats.getItem2()));
        holder.item4.setImageResource(LOLChatApplication.getItemResourceFromId(stats.getItem3()));
        holder.item5.setImageResource(LOLChatApplication.getItemResourceFromId(stats.getItem4()));
        holder.item6.setImageResource(LOLChatApplication.getItemResourceFromId(stats.getItem5()));
        holder.item7.setImageResource(LOLChatApplication.getItemResourceFromId(stats.getItem6()));

        for (int i = 1; i < holder.champ.length; i++) {
            holder.champ[i].setVisibility(View.GONE);
            holder.sum[i].setVisibility(View.GONE);
        }
        holder.champ[0].setImageResource(LOLChatApplication.getChampionResourceFromId(game.getChampionId()));
        int team1 = 0, team2 = 0;
        for (int i = 0; i < game.getFellowPlayers().size(); i++) {
            Player player = game.getFellowPlayers().get(i);
            int index;
            if (player.getTeamId() == game.getTeamId()) {
                index = team1 + 1;
                team1++;
            } else {
                index = team2 + 5;
                team2++;
            }
            holder.champ[index].setVisibility(View.VISIBLE);
            holder.sum[index].setVisibility(View.VISIBLE);
            holder.champ[index].setImageResource(LOLChatApplication.getChampionResourceFromId(player.getChampionId()));
        }
        return convertView;
    }

    @Override
    public Game getGroup(int groupPosition) {
        return recentGames.get(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupHolder {
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

    private class ChildHolder {
        ImageView summonerSpell1;
        ImageView summonerSpell2;
        ImageView item1;
        ImageView item2;
        ImageView item3;
        ImageView item4;
        ImageView item5;
        ImageView item6;
        ImageView item7;
        ImageView[] champ;
        TextView[] sum;
    }
}
