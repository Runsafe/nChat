package no.runsafe.nchat.chat;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.database.MuteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MuteHandler implements IConfigurationChanged
{
	public MuteHandler(MuteDatabase muteDatabase)
	{
		this.muteDatabase = muteDatabase;
	}

	public void loadMuteList()
	{
		mutedPlayers.clear();
		mutedPlayers.addAll(this.muteDatabase.getMuteList());
	}

	public boolean isPlayerMuted(IPlayer player)
	{
		return (this.isPlayerMuted(player.getName()) && !player.hasPermission("runsafe.nchat.mute.exempt"));
	}

	private boolean isPlayerMuted(String playerName)
	{
		return (this.serverMute || this.mutedPlayers.contains(playerName));
	}

	public void mutePlayer(IPlayer player)
	{
		this.mutePlayer(player.getName());
	}

	public void mutePlayer(String playerName)
	{
		this.mutedPlayers.add(playerName);
		this.muteDatabase.mutePlayer(playerName);
	}

	public void unMutePlayer(IPlayer player)
	{
		this.unMutePlayer(player.getName());
	}

	public void unMutePlayer(String playerName)
	{
		this.mutedPlayers.remove(playerName);
		this.muteDatabase.unMutePlayer(playerName);
	}

	public void muteServer()
	{
		this.serverMute = true;
	}

	public void unMuteServer()
	{
		this.serverMute = false;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.serverMute = iConfiguration.getConfigValueAsBoolean("spamControl.muteChat");
		this.loadMuteList();
	}

	private final List<String> mutedPlayers = new ArrayList<String>();
	private final MuteDatabase muteDatabase;
	private boolean serverMute;
}
