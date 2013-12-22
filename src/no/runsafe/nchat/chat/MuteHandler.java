package no.runsafe.nchat.chat;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.nchat.database.MuteDatabase;

import java.util.ArrayList;
import java.util.Collection;

public class MuteHandler implements IConfigurationChanged
{
	public MuteHandler(MuteDatabase muteDatabase)
	{
		this.muteDatabase = muteDatabase;
	}

	public void loadMuteList()
	{
		mutedPlayers.clear();
		mutedPlayers.addAll(muteDatabase.getMuteList());
	}

	public boolean isPlayerMuted(ICommandExecutor player)
	{
		return isPlayerMuted(player.getName()) && !player.hasPermission("runsafe.nchat.mute.exempt");
	}

	private boolean isPlayerMuted(String playerName)
	{
		return serverMute || mutedPlayers.contains(playerName);
	}

	public void mutePlayer(ICommandExecutor player)
	{
		mutePlayer(player.getName());
	}

	public void mutePlayer(String playerName)
	{
		mutedPlayers.add(playerName);
		muteDatabase.mutePlayer(playerName);
	}

	public void unMutePlayer(ICommandExecutor player)
	{
		unMutePlayer(player.getName());
	}

	public void unMutePlayer(String playerName)
	{
		mutedPlayers.remove(playerName);
		muteDatabase.unMutePlayer(playerName);
	}

	public void muteServer()
	{
		serverMute = true;
	}

	public void unMuteServer()
	{
		serverMute = false;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		serverMute = configuration.getConfigValueAsBoolean("spamControl.muteChat");
		loadMuteList();
	}

	private final Collection<String> mutedPlayers = new ArrayList<String>(0);
	private final MuteDatabase muteDatabase;
	private boolean serverMute;
}
