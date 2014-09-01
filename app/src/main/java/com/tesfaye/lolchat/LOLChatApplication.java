package com.tesfaye.lolchat;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class LOLChatApplication extends Application{
    private HashMap<Integer, Bitmap> profileIcons = new HashMap<Integer, Bitmap>();
    public synchronized Bitmap getProfileImage(int id) {
        final int iconId;
        if(id >= 0)
        {
            iconId = id;
        }else
        {
            iconId = 0;//some clients set their icon to -1
        }
        if (!profileIcons.containsKey(iconId)) {
            try
            {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL("http://ddragon.leagueoflegends.com/cdn/4.14.2/img/profileicon/" + iconId + ".png").getContent());
                            profileIcons.put(iconId, bitmap);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
                t.join();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return profileIcons.get(iconId);
    }
}
