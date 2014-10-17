package com.tesfayeabel.lolchat;

import java.util.List;

import jriot.objects.ChampionList;
import jriot.objects.SummonerSpellList;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Abel Tesfaye on 10/16/2014.
 */
public interface RiotApi {

    @GET("/api/lol/static-data/{region}/v1.2/champion")
    public void getChampions(Callback<ChampionList> cb);

    @GET("/api/lol/static-data/{region}/v1.2/summoner-spell")
    public void getSummonerSpells(Callback<SummonerSpellList> cb);

    @GET("/api/lol/static-data/{region}/v1.2/versions")
    public void getVersion(Callback<List<String>> cb);
}
