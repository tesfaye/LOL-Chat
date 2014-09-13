package com.tesfayeabel.lolchat;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LOLChatApplication extends Application {
    private static String clientVersion;

    public static String getRiotResourceURL() {
        return "http://ddragon.leagueoflegends.com/cdn/" + clientVersion;
    }

    private static String getString(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        return response.toString();
    }

    public static Drawable loadChampionImage(Context context, String name) {
        Resources resources = context.getResources();
        name = name.toLowerCase().replace("'", "").replace(" ", "");
        if (name.equals("wukong"))
            name = "monkeyking";
        return resources.getDrawable(resources.getIdentifier("champion_" + name, "drawable", context.getPackageName()));
    }

    public static String getGameSubType(String name) {
        if (name.equals("NONE"))
            return "Custom";
        if (name.equals("RANKED_SOLO_5x5") || name.equals("RANKED_TEAM_3x3") || name.equals("RANKED_TEAM_5x5"))
            return "Ranked";
        if (name.equals("BOT"))
            return "Co-op vs AI";
        return "Normal";
    }

    public static String getMapName(int id)
    {
        switch(id)
        {
            case 1:
                return "Summoner's Rift";
            case 2:
                return "Summoner's Rift";
            case 3:
                return "The Proving Grounds";
            case 4:
                return "Twisted Treeline";
            case 8:
                return "The Crystal Scar";
            case 10:
                return "Twisted Treeline";
            case 12:
                return "Howling Abyss";
            default:
                return "";
        }
    }
    public void onCreate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(getString("http://ddragon.leagueoflegends.com/realms/na.json"));
                    clientVersion = json.getString("v");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }).start();
    }
}
