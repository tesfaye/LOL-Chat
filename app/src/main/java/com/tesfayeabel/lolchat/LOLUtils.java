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

import com.github.theholywaffle.lolchatapi.LolStatus;

import jriot.main.JRiot;
import jriot.main.JRiotException;
import jriot.objects.League;
import jriot.objects.LeagueEntry;
import jriot.objects.PlayerStatsSummary;
import jriot.objects.Summoner;

/**
 * Created by Abel Tesfaye on 9/20/2014.
 */
public class LOLUtils {
    //TODO: USE STRINGS.XML FILE
    public static String getGameSubType(String name) {
        if (name.equals("NONE"))
            return "Custom";
        if (name.equals("RANKED_SOLO_5x5") || name.equals("RANKED_TEAM_3x3") || name.equals("RANKED_TEAM_5x5"))
            return "Ranked";
        if (name.equals("BOT"))
            return "Co-op vs AI";
        return "Normal";
    }

    public static String getGameMode(String name) {
        if (name.equals("CLASSIC"))
            return "Classic";
        if (name.equals("ODIN"))
            return "Dominion";
        if (name.equals("ARAM"))
            return "ARAM";
        if (name.equals("TUTORIAL"))
            return "Tutorial";
        if (name.equals("ONEFORALL"))
            return "One for All";
        if (name.equals("ASCENSION"))
            return "Ascension";
        return "?";
    }

    public static String getStatus(LolStatus.GameStatus status, String gameType) {
        switch (status) {
            case TEAM_SELECT:
                return "In Team Select";
            case HOSTING_NORMAL_GAME:
                return "Creating Normal Game";
            case HOSTING_PRACTICE_GAME:
                return "Creating Practice Game";
            case HOSTING_RANKED_GAME:
                return "Creating Ranked Game";
            case HOSTING_COOP_VS_AI_GAME:
                return "Creating Co-op vs AI Game";
            case IN_QUEUE:
                return "In Queue";
            case SPECTATING:
                return "Spectating";
            case OUT_OF_GAME:
                return "Out Of Game";
            case CHAMPION_SELECT:
                return "In Champion Select";
            case IN_GAME:
                gameType = gameType.substring(0,1) + gameType.substring(1).toLowerCase();
                return gameType +  " Game";
            case IN_TEAMBUILDER:
                return "Team Builder";
            case TUTORIAL:
                return "Tutorial Game";
            default:
                return "?";
        }
    }

    public static String getMapName(int id) {
        switch (id) {
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
            case 11:
                return "Summoner's Rift (Beta)";
            case 12:
                return "Howling Abyss";
            default:
                return "";
        }
    }

    /**
     * @param summoner to get status of
     * @param jRiot    riot api
     * @return status of summoner
     */
    public static LolStatus getStatus(Summoner summoner, JRiot jRiot) {
        LolStatus lolStatus = new LolStatus();
        try {
            lolStatus.setLevel((int) summoner.getSummonerLevel());
            lolStatus.setProfileIconId(summoner.getProfileIconId());
            int normalWins = 0;
            for (PlayerStatsSummary p : jRiot.getPlayerStatsSummaryList(summoner.getId(), 4).getPlayerStatSummaries()) {
                if (p.getPlayerStatSummaryType().equals("RankedSolo5x5")) {
                    lolStatus.setRankedWins(p.getWins());
                }
                if (p.getPlayerStatSummaryType().equals("Unranked")) {
                    normalWins += p.getWins();
                }
                if (p.getPlayerStatSummaryType().equals("Unranked3x3")) {
                    normalWins += p.getWins();
                }
            }
            lolStatus.setNormalWins(normalWins);
            if (lolStatus.getRankedWins() != -1) {
                for (League league : jRiot.getLeagues(summoner.getId())) {
                    if (league.getQueue().equals(LolStatus.Queue.RANKED_SOLO_5x5.name())) {
                        lolStatus.setRankedLeagueTier(LolStatus.Tier.valueOf(league.getTier()));
                        for (LeagueEntry e : league.getEntries()) {
                            if (e.getPlayerOrTeamId().equals(league.getParticipantId())) {
                                lolStatus.setRankedLeagueDivision(LolStatus.Division.valueOf(e.getDivision()));
                            }
                        }
                        lolStatus.setRankedLeagueName(league.getName());
                    }
                }
            }
        } catch (JRiotException exception) {
            exception.printStackTrace();
        }
        return lolStatus;
    }
}
