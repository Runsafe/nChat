package no.runsafe.nchat.events;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.player.IPlayerQuitEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerQuitEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;

public class LeaveEvent implements IPlayerQuitEvent, IConfigurationChanged
{
	public LeaveEvent(ChatHandler chatHandler)
	{
		this.chatHandler = chatHandler;
	}

	@Override
	public void OnPlayerQuit(RunsafePlayerQuitEvent runsafePlayerQuitEvent)
	{
		RunsafePlayer player = runsafePlayerQuitEvent.getPlayer();
		runsafePlayerQuitEvent.setQuitMessage(this.chatHandler.formatPlayerSystemMessage(this.leaveServerMessage, player));
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.leaveServerMessage = iConfiguration.getConfigValueAsString("chatMessage.leaveServer");
	}

	private ChatHandler chatHandler;
	private String leaveServerMessage;
}
