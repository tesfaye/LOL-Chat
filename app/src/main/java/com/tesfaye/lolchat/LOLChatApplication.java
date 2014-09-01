package com.tesfaye.lolchat;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class LOLChatApplication extends Application{
    private HashMap<Integer, Bitmap> profileIcons = new HashMap<Integer, Bitmap>();
    public synchronized Bitmap getProfileImage(final int id) {
        if (!profileIcons.containsKey(id)) {
            try
            {
                final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL("http://ddragon.leagueoflegends.com/cdn/4.14.2/img/profileicon/" + id + ".png").getContent());
                profileIcons.put(id, bitmap);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return profileIcons.get(id);
    }
}
