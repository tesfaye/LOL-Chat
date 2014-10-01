package com.tesfayeabel.lolchat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abel Tesfaye on 9/26/2014.
 */
public class RecentConversation implements Parcelable {

    public static final Parcelable.Creator<RecentConversation> CREATOR
            = new Parcelable.Creator<RecentConversation>() {
        public RecentConversation createFromParcel(Parcel in) {
            return new RecentConversation(in);
        }

        public RecentConversation[] newArray(int size) {
            return new RecentConversation[size];
        }
    };
    private int profileIconId;
    private String name;
    private long lastUpdate;
    private String lastMessage;

    public RecentConversation(int profileIconId, String name, String lastMessage, long lastUpdate) {
        this.profileIconId = profileIconId;
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastUpdate = lastUpdate;
    }

    public RecentConversation(int profileIconId, String name) {
        this(profileIconId, name, null, -1);
    }

    private RecentConversation(Parcel in) {
        profileIconId = in.readInt();
        name = in.readString();
        lastMessage = in.readString();
        lastUpdate = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(profileIconId);
        out.writeString(name);
        out.writeString(lastMessage);
        out.writeLong(lastUpdate);
    }

    public int describeContents() {
        return 0;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
    }

    public String getName() {
        return name;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
