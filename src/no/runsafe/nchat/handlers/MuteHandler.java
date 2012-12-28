package no.runsafe.nchat.handlers;

import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.database.MuteDatabase;

import java.util.List;

public class MuteHandler
{
	public MuteHandler(MuteDatabase muteDatabase)
	{
		this.muteDatabase = muteDatabase;
		this.loadMuteList();
	}

	public void loadMuteList()
	{
		this.mutedPlayers = this.muteDatabase.getMuteList();
	}

	public boolean isPlayerMuted(RunsafePlayer player)
	{
		return this.isPlayerMuted(player.getName());
	}

	public boolean isPlayerMuted(String playerName)
	{
		return this.mutedPlayers.contains(playerName);
	}

	public void mutePlayer(RunsafePlayer player)
	{
		this.mutePlayer(player.getName());
	}

	public void mutePlayer(String playerName)
	{
		this.mutedPlayers.add(playerName);
		this.muteDatabase.mutePlayer(playerName);
	}

	public void unMutePlayer(RunsafePlayer player)
	{
		this.unMutePlayer(player.getName());
	}

	public void unMutePlayer(String playerName)
	{
		this.mutedPlayers.remove(playerName);
		this.muteDatabase.unMutePlayer(playerName);
	}

	private List<String> mutedPlayers;
	private MuteDatabase muteDatabase;
}
