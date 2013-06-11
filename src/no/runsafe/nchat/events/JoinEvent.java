package no.runsafe.nchat.events;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerJoinEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerJoinEvent;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;

public class JoinEvent implements IPlayerJoinEvent, IConfigurationChanged
{
	public JoinEvent(ChatHandler chatHandler)
	{
		this.chatHandler = chatHandler;
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent runsafePlayerJoinEvent)
	{
		RunsafePlayer player = runsafePlayerJoinEvent.getPlayer();
		runsafePlayerJoinEvent.setJoinMessage(this.chatHandler.formatPlayerSystemMessage(this.joinServerMessage, player));
		this.chatHandler.refreshPlayerTabListName(player);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.joinServerMessage = iConfiguration.getConfigValueAsString("chatMessage.joinServer");
	}

	private String joinServerMessage;
	private final ChatHandler chatHandler;
}
