package com.tesfayeabel.lolchat;

import com.github.theholywaffle.lolchatapi.LolStatus;

import jriot.main.JRiot;
import jriot.main.JRiotException;
import jriot.objects.League;
import jriot.objects.LeagueEntry;
import jriot.objects.PlayerStatsSummary;
import jriot.objects.Summoner;

public class Player {
    private LolStatus lolStatus;

    public Player(String summonerName, JRiot jRiot) {
        lolStatus = new LolStatus();
        try {
            Summoner summoner = jRiot.getSummoner(summonerName);
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
    }

    public LolStatus getStatus() {
        return lolStatus;
    }
}
