package no.runsafe.nchat.chat;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.nchat.database.MuteDatabase;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MuteHandler implements IConfigurationChanged
{
	public MuteHandler(MuteDatabase muteDatabase, IConsole console)
	{
		this.muteDatabase = muteDatabase;
		this.console = console;
	}

	public void loadMuteList()
	{
		mutedPlayers.clear();
		mutedPlayers.putAll(muteDatabase.getMuteList());
		console.logInformation("Loaded %d mutes from database.", mutedPlayers.size());
	}

	public boolean isPlayerMuted(ICommandExecutor player)
	{
		return isPlayerMuted(player.getName()) && !player.hasPermission("runsafe.nchat.mute.exempt");
	}

	private boolean isPlayerMuted(String playerName)
	{
		if (mutedPlayers.containsKey(playerName) && mutedPlayers.get(playerName).getMuteTime().isBeforeNow())
			unMutePlayer(playerName);

		return serverMute || mutedPlayers.containsKey(playerName);
	}

	public boolean isShadowMuted(ICommandExecutor player)
	{
		return isPlayerMuted(player) && mutedPlayers.get(player.getName()).isShadow();
	}

	public void tempMutePlayer(ICommandExecutor player, Period expire)
	{
		tempMutePlayer(player, expire, false);
	}

	public void tempMutePlayer(ICommandExecutor player, Period expire, boolean shadow)
	{
		DateTime limit = DateTime.now().plus(expire);
		String playerName = player.getName();

		mutedPlayers.put(playerName, new MuteEntry(playerName, limit, shadow));
		muteDatabase.tempMutePlayer(playerName, limit, shadow);
	}

	public void mutePlayer(ICommandExecutor player)
	{
		mutePlayer(player.getName());
	}

	public void mutePlayer(String playerName)
	{
		mutePlayer(playerName, false);
	}

	public void mutePlayer(String playerName, boolean shadow)
	{
		mutedPlayers.put(playerName, new MuteEntry(playerName, MuteDatabase.END_OF_TIME, shadow));
		muteDatabase.mutePlayer(playerName, shadow);
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

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		serverMute = configuration.getConfigValueAsBoolean("spamControl.muteChat");
		loadMuteList();
	}

	private final Map<String, MuteEntry> mutedPlayers = new ConcurrentHashMap<String, MuteEntry>(0);
	private final MuteDatabase muteDatabase;
	private final IConsole console;
	private boolean serverMute;
}
