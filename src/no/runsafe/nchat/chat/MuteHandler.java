package no.runsafe.nchat.chat;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.internal.wrapper.player.BukkitPlayer;
import no.runsafe.nchat.database.MuteDatabase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Instant;
import java.time.Duration;

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

	public boolean isPlayerMuted(IPlayer player)
	{
		// Check for fake players
		if (((BukkitPlayer) player).getRaw() == null)
			return false;

		if (mutedPlayers.containsKey(player) && mutedPlayers.get(player).isBefore(Instant.now()))
			unMutePlayer(player);
		return (serverMute || mutedPlayers.containsKey(player)) && !player.hasPermission("runsafe.nchat.mute.exempt");
	}

	public void tempMutePlayer(IPlayer player, Duration expire)
	{
		Instant limit = Instant.now().plus(expire);
		mutedPlayers.put(player, limit);
		muteDatabase.tempMutePlayer(player, limit);
	}

	public void mutePlayer(IPlayer player)
	{
		mutedPlayers.put(player, MuteDatabase.END_OF_TIME);
		muteDatabase.mutePlayer(player);
	}

	public void unMutePlayer(IPlayer player)
	{
		mutedPlayers.remove(player);
		muteDatabase.unMutePlayer(player);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		serverMute = configuration.getConfigValueAsBoolean("spamControl.muteChat");
		loadMuteList();
	}

	private final Map<IPlayer, Instant> mutedPlayers = new ConcurrentHashMap<>(0);
	private final MuteDatabase muteDatabase;
	private final IConsole console;
	private boolean serverMute;
}
