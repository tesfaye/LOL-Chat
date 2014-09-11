package com.tesfayeabel.lolchat;

import android.app.Application;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class LOLChatApplication extends Application {
    private static String clientVersion;
    private static HashMap<Integer, String> maps;

    public static String getRiotResourceURL()
    {
        return "http://ddragon.leagueoflegends.com/cdn/" + clientVersion;
    }

    public static String getMapName(int id)
    {
        return maps.get(id);
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

    public void onCreate() {
        maps = new HashMap<Integer, String>();
        maps.put(1, "Summoner's Rift");
        maps.put(2, "Summoner's Rift");
        maps.put(3, "The Proving Grounds");
        maps.put(4, "Twisted Treeline");
        maps.put(8, "The Crystal Scar");
        maps.put(10, "Twisted Treeline");
        maps.put(12, " Howling Abyss ");
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
