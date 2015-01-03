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

package com.tesfayeabel.lolchat.data;

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

    /**
     * Creates a message object
     *
     * @param sender    the sender
     * @param message   the message
     * @param direction either MessageAdapter.DIRECTION_INCOMING or MessageAdapter.DIRECTION_OUTGOING
     * @param time      time in milliseconds when the message sent or received
     */
    public Message(String sender, String message, int direction, long time) {
        this.sender = sender;
        this.message = message;
        this.direction = direction;
        this.time = time;
    }

    /**
     * See other constructor
     *
     * @param read
     */
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

    public long getTime() {
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
