package no.runsafe.nchat;

import no.runsafe.framework.api.hook.IPlayerDataProvider;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.chat.IgnoreHandler;
import no.runsafe.nchat.chat.MuteHandler;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
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

		data.put("nChat.isMuted", muteHandler.isPlayerMuted(player) ? "True" : "False");
		data.put("nChat.ignoredBy", StringUtils.join(ignoreHandler.getPlayersIgnoring(player), ", "));
		data.put("nChat.ignoring", StringUtils.join(ignoreHandler.getIgnoredPlayers(player), ", "));

		return data;
	}

	private final MuteHandler muteHandler;
	private final IgnoreHandler ignoreHandler;
}
