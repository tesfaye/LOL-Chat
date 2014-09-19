package com.tesfayeabel.lolchat;

import com.github.theholywaffle.lolchatapi.ChatMode;
import com.github.theholywaffle.lolchatapi.LolStatus;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

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

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StaticFriend))
            return false;
        StaticFriend other = (StaticFriend) object;
        return getUserId().equals(other.getUserId());
    }
}
