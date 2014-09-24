package com.tesfayeabel.lolchat;

import java.util.Map;

import jriot.objects.Champion;
import jriot.objects.ChampionList;
import jriot.objects.Summoner;
import jriot.objects.SummonerSpellList;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface RiotRestService {
    @GET("/na/v1.4/summoner/by-name/{names}")
    void getSummonersByName(@Path("name") String names, Callback<Map<String, Summoner>> callback);

    @GET("/na/v1.4/summoner/by-name/{ids}")
    void getSummonersById(@Path("ids") String ids, Callback<Map<String, Summoner>> callback);

    @GET("/static-data/na/v1.2/champion")
    void getChampions(Callback<ChampionList> callback);

    @GET("/static-data/na/v1.2/summoner-spell")
    void getSummonerSpells(Callback<SummonerSpellList> callback);
}
