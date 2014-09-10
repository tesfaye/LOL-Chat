package com.tesfayeabel.lolchat;

import android.app.Application;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LOLChatApplication extends Application {
    private static String clientVersion;

    public static String getRiotResourceURL()
    {
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
