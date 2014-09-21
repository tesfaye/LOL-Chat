package com.tesfayeabel.lolchat;

import android.app.Application;
import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;

public class LOLChatApplication extends Application {
    private static String clientVersion;
    private static SparseArray<String> championArray;
    private static SparseArray<String> summonerSpellArray;

    public static String getRiotResourceURL() {
        return "http://ddragon.leagueoflegends.com/cdn/" + clientVersion;
    }

    private static String getHTML(String url) throws Exception {
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

    public static String getChampionName(int id) {
        return championArray.get(id);
    }

    public static String getSpellName(int id) {
        return summonerSpellArray.get(id).replace("summoner", "summoner_");
    }

    public static int getDrawableIdByName(String name) {
        try {
            Class res = R.drawable.class;
            Field field = res.getField(name);
            return field.getInt(null);
        } catch (Exception e) {
            return 0;
        }
    }

    private SparseArray<String> parseRiotStaticData(String type) throws Exception {
        JSONObject champs = new JSONObject(getHTML("https://na.api.pvp.net/api/lol/static-data/na/v1.2/" + type + "?api_key=" + getString(R.string.api_riot)));
        JSONObject data = champs.getJSONObject("data");
        JSONArray array = data.names();
        SparseArray<String> ret = new SparseArray<String>();
        for (int i = 0; i < array.length(); i++) {
            String name = array.get(i).toString();
            ret.put(data.getJSONObject(name).getInt("id"), name.toLowerCase());
        }
        return ret;
    }

    public void onCreate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject version = new JSONObject(getHTML("http://ddragon.leagueoflegends.com/realms/na.json"));
                    clientVersion = version.getString("v");
                    championArray = parseRiotStaticData("champion");
                    summonerSpellArray = parseRiotStaticData("summoner-spell");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }).start();
    }
}
