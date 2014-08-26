package com.github.theholywaffle.lolchatapi;

/*
 * #%L
 * League of Legends XMPP Chat Library
 * %%
 * Copyright (C) 2014 Bert De Geyter
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.util.ArrayList;

/**
 * Represents a regions chatserver.
 *
 */
public enum ChatServer {

	/**
	 * Brazil
	 */
	BR("Brazil", "chat.br.lol.riotgames.com", "br.api.pvp.net"),
	/**
	 * Europe Nordic and East
	 */
	EUNE("Europe Nordic and East", "chat.eun1.riotgames.com", "eune.api.pvp.net"),
	/**
	 * Europe West
	 */
	EUW("Europe West", "chat.euw1.lol.riotgames.com", "euw.api.pvp.net"),
	/**
	 * Korea
	 */
	KR("Korea", "chat.kr.lol.riotgames.com", "kr.api.pvp.net"),
	/**
	 * Latin America North
	 */
	LAN("Latin America North", "chat.la1.lol.riotgames.com", "lan.api.pvp.net"),
	/**
	 * Latin America South
	 */
	LAS("Latin America South", "chat.la2.lol.riotgames.com", "las.api.pvp.net"),
	/**
	 * North-America
	 */
	NA("North-America", "chat.na1.lol.riotgames.com", "na.api.pvp.net"),
	/**
	 * Oceania
	 */
	OCE("Oceania", "chat.oc1.lol.riotgames.com", "oce.api.pvp.net"),
	/**
	 * Public Beta Environment
	 */
	PBE("Public Beta Environment", "chat.pbe1.lol.riotgames.com", null),
	/**
	 * Phillipines
	 */
	PH("Phillipines", "chatph.lol.garenanow.com", null),
	/**
	 * Russia
	 */
	RU("Russia", "chat.ru.lol.riotgames.com", "ru.api.pvp.net"),
	/**
	 * Thailand
	 */
	TH("Thailand", "chatth.lol.garenanow.com", null),
	/**
	 * Turkey
	 */
	TR("Turkey", "chat.tr.lol.riotgames.com", "tr.api.pvp.net"),
	/**
	 * Taiwan
	 */
	TW("Taiwan", "chattw.lol.garenanow.com", null),
	/**
	 * Vietnam
	 */
	VN("Vietnam", "chatvn.lol.garenanow.com", null);

    public String name;
	public String host;
	public String api;

	ChatServer(String name, String host, String api) {
		this.name = name;
        this.host = host;
		this.api = api;
	}
    public ArrayList<ChatServer> getAllChatServers()
    {
        ArrayList<ChatServer> servers = new ArrayList<ChatServer>();
        for(ChatServer server: getClass().getEnumConstants())
        {
            if(server.api != null)
                servers.add(server);
        }
        return servers;
    }
	@Override
	public String toString() {
		return name().toLowerCase();
	}

}
