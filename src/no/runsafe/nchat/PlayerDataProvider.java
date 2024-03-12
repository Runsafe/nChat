package no.runsafe.nchat;

import no.runsafe.framework.api.hook.IPlayerDataProvider;
import no.runsafe.framework.api.hook.PlayerData;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.IgnoreHandler;
import no.runsafe.nchat.chat.MuteHandler;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerDataProvider implements IPlayerDataProvider
{
	public PlayerDataProvider(MuteHandler muteHandler, IgnoreHandler ignoreHandler)
	{
		this.muteHandler = muteHandler;
		this.ignoreHandler = ignoreHandler;
	}

	@Override
	public void GetPlayerData(PlayerData data)
	{
		data.addData("nchat.isMuted", () -> muteHandler.isPlayerMuted(data.getPlayer()) ? "True" : "False");
		data.addData(
			"nchat.ignoredBy",
			() ->
			{
				List<IPlayer> ignoringPlayers = ignoreHandler.getPlayersIgnoring(data.getPlayer());
				return ignoringPlayers.isEmpty() ? "Nobody" : StringUtils.join(
					ignoringPlayers.stream().map(IPlayer::getName).collect(Collectors.toList()),
					", "
				);
			}
		);
		data.addData(
			"nchat.ignoring",
			() ->
			{
				List<IPlayer> ignoredPlayers = ignoreHandler.getIgnoredPlayers(data.getPlayer());
				return ignoredPlayers.isEmpty() ? "Nobody" : StringUtils.join(
					ignoredPlayers.stream().map(IPlayer::getName).collect(Collectors.toList()),
					", "
				);
			}
		);
	}

	private final MuteHandler muteHandler;
	private final IgnoreHandler ignoreHandler;
}
