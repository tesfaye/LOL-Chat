package com.tesfayeabel.lolchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tesfayeabel.lolchat.LOLChatApplication;
import com.tesfayeabel.lolchat.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import jriot.main.JRiot;
import jriot.main.JRiotException;
import jriot.objects.Game;
import jriot.objects.RawStats;

public class RecentGamesAdapter extends BaseAdapter {

    private List<Game> games;
    private Context context;

    public RecentGamesAdapter(Context context, List<Game> list) {
        this.context = context;
        games = list;
    }

    //Returns how many messages are in the list
    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Game getItem(int i) {
        return games.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //Tells your app how many possible layouts there are
    //In our case, right and left messages are our only 2 options
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //This returns either DIRECTION_INCOMING or DIRECTION_OUTGOING
    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.recent_game_item, viewGroup, false);
            holder = new ViewHolder();
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
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        final Game game = getItem(i);
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

    public class ViewHolder {
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
}
