package com.tesfayeabel.lolchat;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    public static final Parcelable.Creator<Message> CREATOR
            = new Parcelable.Creator<Message>() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
    private String sender;
    private String message;
    private int direction;
    private long time;

    public Message(String sender, String message, int direction, long time) {
        this.sender = sender;
        this.message = message;
        this.direction = direction;
        this.time = time;
    }

    public Message(String read) {
        String[] split = read.split(":", 4);
        this.direction = Integer.parseInt(split[0]);
        this.time = Long.parseLong(split[1]);
        this.sender = split[2];
        this.message = split[3];
    }

    private Message(Parcel in) {
        sender = in.readString();
        message = in.readString();
        direction = in.readInt();
        time = in.readLong();
    }

    public String toString() {
        return direction + ":" + time + ":" + sender + ":" + message;
    }

    public int getDirection() {
        return direction;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public long getTime()
    {
        return time;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(sender);
        out.writeString(message);
        out.writeInt(direction);
        out.writeLong(time);
    }

    public int describeContents() {
        return 0;
    }
}
