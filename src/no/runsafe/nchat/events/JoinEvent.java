package no.runsafe.nchat.events;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerJoinEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerJoinEvent;
import no.runsafe.nchat.chat.PlayerChatEngine;
import no.runsafe.nchat.tablist.PlayerTablistNameHandler;

public class JoinEvent implements IPlayerJoinEvent, IConfigurationChanged
{
	public JoinEvent(PlayerChatEngine chatEngine, PlayerTablistNameHandler tabListHandler)
	{
		this.chatEngine = chatEngine;
		this.tabListHandler = tabListHandler;
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent event)
	{
		event.setJoinMessage(null);
		IPlayer player = event.getPlayer();
		chatEngine.broadcastMessage(joinServerMessage.replace("#player", player.getPrettyName()));
		tabListHandler.refreshPlayerTabListName(player);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		joinServerMessage = configuration.getConfigValueAsString("chatMessage.joinServer");
	}

	private String joinServerMessage;
	private final PlayerChatEngine chatEngine;
	private final PlayerTablistNameHandler tabListHandler;
}
