package no.runsafe.nchat.handlers;

import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.List;

public class AfkHandler
{
	public AfkHandler(ChatHandler chatHandler)
	{
		this.chatHandler = chatHandler;
	}

	public void setPlayerAfk(RunsafePlayer player)
	{
		String playerName = player.getName();
		if (!this.afkPlayers.contains(playerName))
			this.afkPlayers.add(playerName);


	}

	public void setPlayerReturned(RunsafePlayer player)
	{
		String playerName = player.getName();
		if (this.afkPlayers.contains(playerName))
			this.afkPlayers.remove(playerName);
	}

	public boolean playerIsAfk(RunsafePlayer player)
	{
		return this.afkPlayers.contains(player.getName());
	}

	private List<String> afkPlayers;
	private ChatHandler chatHandler;
}
