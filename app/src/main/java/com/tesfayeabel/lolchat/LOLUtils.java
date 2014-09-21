package com.tesfayeabel.lolchat;

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
            case 12:
                return "Howling Abyss";
            default:
                return "";
        }
    }
}
