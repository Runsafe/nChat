package no.runsafe.nchat.events;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerJoinEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerJoinEvent;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.chat.ChatEngine;
import no.runsafe.nchat.tablist.TabListHandler;

public class JoinEvent implements IPlayerJoinEvent, IConfigurationChanged
{
	public JoinEvent(ChatEngine chatEngine, TabListHandler tabListHandler)
	{
		this.chatEngine = chatEngine;
		this.tabListHandler = tabListHandler;
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent event)
	{
		event.setJoinMessage(null);
		RunsafePlayer player = event.getPlayer();
		chatEngine.broadcastMessage(joinServerMessage.replace("#player", player.getPrettyName()));
		tabListHandler.refreshPlayerTabListName(player);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.joinServerMessage = configuration.getConfigValueAsString("chatMessage.joinServer");
	}

	private String joinServerMessage;
	private final ChatEngine chatEngine;
	private final TabListHandler tabListHandler;
}
