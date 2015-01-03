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

package com.tesfayeabel.lolchat;

import android.app.Application;
import android.util.SparseArray;

import com.github.theholywaffle.lolchatapi.ChatServer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import jriot.main.JRiotException;
import jriot.objects.Champion;
import jriot.objects.ChampionList;
import jriot.objects.SummonerSpell;
import jriot.objects.SummonerSpellList;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LOLChatApplication extends Application {
    private static String clientVersion;
    private static SparseArray<String> championArray;
    private static SparseArray<String> summonerSpellArray;

    private static String getRiotResourceURL() {
        return "http://ddragon.leagueoflegends.com/cdn/" + clientVersion;
    }

    public static String getProfileIconURL(int iconId) {
        return getRiotResourceURL() + "/img/profileicon/" + iconId + ".png";
    }

    public static int getChampionResourceFromId(int id) {
        return getDrawableIdByName(championArray.get(id));
    }

    public static int getSpellResourceFromId(int id) {
        return getDrawableIdByName(summonerSpellArray.get(id));
    }

    public static int getItemResourceFromId(int id) {
        return getDrawableIdByName("item_" + id);
    }

    private static int getDrawableIdByName(String name) {
        try {
            Class res = R.drawable.class;
            Field field = res.getField(name);
            return field.getInt(null);
        } catch (Exception e) {
            return 0;
        }
    }

    private static RiotApi create(final ChatServer chatServer, final String apiKey) {
        return new RestAdapter.Builder()
                .setEndpoint("https://" + chatServer.api)
                .setRequestInterceptor(new RequestInterceptor() {

                    @Override
                    public void intercept(RequestFacade f) {
                        f.addEncodedQueryParam("api_key", apiKey);
                        f.addEncodedPathParam("region", chatServer.name().toLowerCase());
                    }
                }).build().create(RiotApi.class);
    }

    private SparseArray<String> parseChampionMap(Map<String, Champion> map) {
        SparseArray<String> ret = new SparseArray<String>();
        for (Map.Entry<String, Champion> championEntry : map.entrySet()) {
            ret.put((int) championEntry.getValue().getId(), "champion_" + championEntry.getValue().getKey().toLowerCase());
        }
        return ret;
    }

    private SparseArray<String> parseSummonerSpellMap(Map<String, SummonerSpell> map) {
        SparseArray<String> ret = new SparseArray<String>();
        for (Map.Entry<String, SummonerSpell> summonerSpellEntry : map.entrySet()) {
            ret.put(summonerSpellEntry.getValue().getId(), summonerSpellEntry.getValue().getKey().toLowerCase().replace("summoner", "summoner_"));
        }
        return ret;
    }

    public void onCreate() {
        RiotApi riotApi = create(ChatServer.NA, getString(R.string.api_riot));
        riotApi.getChampions(new Callback<ChampionList>() {
            @Override
            public void success(ChampionList championList, Response response) {
                championArray = parseChampionMap(championList.getData());
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println(new JRiotException(error.getResponse().getStatus()));
            }
        });
        riotApi.getSummonerSpells(new Callback<SummonerSpellList>() {
            @Override
            public void success(SummonerSpellList summonerSpellList, Response response) {
                summonerSpellArray = parseSummonerSpellMap(summonerSpellList.getData());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        riotApi.getVersion(new Callback<List<String>>() {
            @Override
            public void success(List<String> versions, Response response) {
                clientVersion = versions.get(0);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
