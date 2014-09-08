package com.tesfayeabel.lolchat;

import com.github.theholywaffle.lolchatapi.LolStatus;

import jriot.main.JRiot;
import jriot.main.JRiotException;
import jriot.objects.League;
import jriot.objects.LeagueEntry;
import jriot.objects.PlayerStatsSummary;
import jriot.objects.Summoner;

public class Player {
    private int rankedWins;
    private String rankedLeagueTier;
    private String rankedDivision;
    private String rankedLeagueName;
    private int summonerLevel;
    private int profileIcon;
    public Player(String summonerName, JRiot jRiot)
    {
        try{
            Summoner summoner = jRiot.getSummoner(summonerName);
            summonerLevel = (int)summoner.getSummonerLevel();
            profileIcon = summoner.getProfileIconId();
            for(PlayerStatsSummary p: jRiot.getPlayerStatsSummaryList(summoner.getId(), 4).getPlayerStatSummaries())
            {
                if(p.getPlayerStatSummaryType().equals("RankedSolo5x5"))
                {
                    rankedWins = p.getWins();
                }
            }
            for(League league: jRiot.getLeagues(summoner.getId()))
            {
                if(league.getQueue().equals(LolStatus.Queue.RANKED_SOLO_5x5.name()))
                {
                    rankedLeagueTier = league.getTier();
                    for(LeagueEntry e: league.getEntries())
                    {
                        if(e.getPlayerOrTeamId().equals(league.getParticipantId()))
                        {
                            rankedDivision = e.getDivision();
                        }
                    }
                    rankedLeagueName = league.getName();
                }
            }
        } catch(JRiotException exception)
        {
            exception.printStackTrace();
        }
    }

    public int getRankedWins() {
        return rankedWins;
    }

    public String getRankedLeagueTier() {
        return rankedLeagueTier;
    }

    public String getRankedDivision() {
        return rankedDivision;
    }

    public String getRankedLeagueName() {
        return rankedLeagueName;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public int getProfileIcon() {
        return profileIcon;
    }
}
