package com.tesfayeabel.lolchat;

import com.github.theholywaffle.lolchatapi.ChatMode;
import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Abel Tesfaye on 9/16/2014.
 */
public class StaticFriend {
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

    public void setStatus(LolStatus lolStatus) {
        this.lolStatus = lolStatus;
    }

    public ChatMode getChatMode() {
        return chatMode;
    }

    public void setChatMode(ChatMode chatMode) {
        this.chatMode = chatMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getFullStatus() {
        StringBuilder fullStatus = new StringBuilder();
        LolStatus.GameStatus gameStatus = getStatus().getGameStatus();
        fullStatus.append(getStatus().getStatusMessage() + "\n");
        if (gameStatus == null)
            fullStatus.append("Online");
        else
            fullStatus.append(gameStatus.internal());
        if (gameStatus == LolStatus.GameStatus.IN_GAME) {
            Date current = new Date();
            fullStatus.append(" as " + getStatus().getSkin());
            fullStatus.append(" for " + TimeUnit.MILLISECONDS.toMinutes(new Date(current.getTime() - getStatus().getTimestamp().getTime()).getTime()) + " minutes");
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
}
