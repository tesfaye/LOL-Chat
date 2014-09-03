package com.tesfaye.lolchat;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable{
    private String sender;
    private String message;
    private int direction;
    public Message(String sender, String message, int direction)
    {
        this.sender = sender;
        this.message = message;
        this.direction = direction;
    }
    public Message(String read)
    {
        String[] split = read.split(":");
        this.sender = split[1];
        this.message = split[2];
        this.direction = Integer.parseInt(split[0]);
    }
    public String toString()
    {
        return direction + ":" + sender + ":" + message;
    }

    public int getDirection()
    {
        return direction;
    }

    public String getMessage()
    {
        return message;
    }

    public String getSender()
    {
        return sender;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(sender);
        out.writeString(message);
        out.writeInt(direction);
    }

    public static final Parcelable.Creator<Message> CREATOR
            = new Parcelable.Creator<Message>() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
    private Message(Parcel in) {
        sender = in.readString();
        message = in.readString();
        direction = in.readInt();
    }
    public int describeContents() {
        return 0;
    }
}
