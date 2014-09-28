package com.tesfayeabel.lolchat;

import com.github.theholywaffle.lolchatapi.ChatMode;
import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.util.concurrent.TimeUnit;

/**
 * Created by Abel Tesfaye on 9/16/2014.
 */
public class StaticFriend implements Comparable<StaticFriend>{
    private LolStatus lolStatus;
    private ChatMode chatMode;
    private String name;
    private String userId;
    private boolean online;

    public StaticFriend(String name, String userId, ChatMode chatMode, LolStatus lolStatus, boolean online) {
        this.name = name;
        this.userId = userId;
        this.lolStatus = lolStatus;
        this.chatMode = chatMode;
        this.online = online;
    }

    public StaticFriend(Friend friend) {
        this(friend.getName(), friend.getUserId(), friend.getChatMode(), friend.getStatus(), friend.isOnline());
    }

    public LolStatus getStatus() {
        return lolStatus;
    }

    public ChatMode getChatMode() {
        return chatMode;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isOnline() {
        return online;
    }

    public String getFullStatus() {
        StringBuilder fullStatus = new StringBuilder();
        LolStatus.GameStatus gameStatus = getStatus().getGameStatus();
        fullStatus.append(getStatus().getStatusMessage() + "\n");
        if (gameStatus == null)
            fullStatus.append("Online");
        else
            fullStatus.append(LOLUtils.getStatus(gameStatus));
        if (gameStatus == LolStatus.GameStatus.IN_GAME) {
            fullStatus.append(" as " + getStatus().getSkin());
            fullStatus.append(" for " + TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - getStatus().getTimestamp().getTime()) + " minutes");
        }
        return fullStatus.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StaticFriend))
            return false;
        StaticFriend other = (StaticFriend) object;
        return getUserId().equals(other.getUserId());
    }

    @Override
    public int compareTo(StaticFriend staticFriend) {
       if(isOnline() && getChatMode() != staticFriend.getChatMode()) {//if online and different chatmodes, compare chatmode, else compare name
           return getChatMode().compareTo(staticFriend.getChatMode());
       }
       return getName().toLowerCase().compareTo(staticFriend.getName().toLowerCase());
    }
}
