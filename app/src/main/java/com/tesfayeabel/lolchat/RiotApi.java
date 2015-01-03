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
