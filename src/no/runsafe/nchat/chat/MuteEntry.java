package no.runsafe.nchat.chat;

import org.joda.time.DateTime;

public class MuteEntry
{
	public MuteEntry(String playerName, DateTime temp, boolean shadow)
	{
		this.playerName = playerName;
		this.temp = temp;
		this.shadow = shadow;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public DateTime getMuteTime()
	{
		return temp;
	}

	public boolean isShadow()
	{
		return shadow;
	}

	private final String playerName;
	private final DateTime temp;
	private final boolean shadow;
}
