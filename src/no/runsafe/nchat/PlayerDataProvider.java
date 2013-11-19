package no.runsafe.nchat;

import no.runsafe.framework.api.hook.IPlayerDataProvider;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.chat.IgnoreHandler;
import no.runsafe.nchat.chat.MuteHandler;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDataProvider implements IPlayerDataProvider
{
	public PlayerDataProvider(MuteHandler muteHandler, IgnoreHandler ignoreHandler)
	{
		this.muteHandler = muteHandler;
		this.ignoreHandler = ignoreHandler;
	}

	@Override
	public Map<String, String> GetPlayerData(RunsafePlayer player)
	{
		Map<String, String> data = new HashMap<String, String>();

		data.put("nchat.ismuted", muteHandler.isPlayerMuted(player) ? "True" : "False");

		List<String> ignoringPlayers = ignoreHandler.getPlayersIgnoring(player);
		data.put("nchat.ignoredby", ignoringPlayers.size() == 0 ? "Nobody" : StringUtils.join(ignoringPlayers, ", "));

		List<String> ignoredPlayers = ignoreHandler.getIgnoredPlayers(player);
		data.put("nchat.ignoring", ignoredPlayers.size() == 0 ? "Nobody" : StringUtils.join(ignoredPlayers, ", "));

		return data;
	}

	private final MuteHandler muteHandler;
	private final IgnoreHandler ignoreHandler;
}
