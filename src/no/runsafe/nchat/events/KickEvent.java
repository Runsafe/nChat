package no.runsafe.nchat.events;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.player.IPlayerKickEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerKickEvent;
import no.runsafe.nchat.handlers.ChatHandler;

public class KickEvent implements IPlayerKickEvent, IConfigurationChanged
{
	public KickEvent(ChatHandler chatHandler)
	{
		this.chatHandler = chatHandler;
	}

	@Override
	public void OnPlayerKick(RunsafePlayerKickEvent runsafePlayerKickEvent)
	{
		runsafePlayerKickEvent.setLeaveMessage(
			(this.suppressKickMessages) ? null : this.chatHandler.formatPlayerSystemMessage(
					this.kickMessage.replace("#reason", runsafePlayerKickEvent.getReason()),
					runsafePlayerKickEvent.getPlayer()
				)
		);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.kickMessage = iConfiguration.getConfigValueAsString("chatMessage.kicked");
		this.suppressKickMessages = iConfiguration.getConfigValueAsBoolean("spamControl.suppressKickMessages");
	}

	private String kickMessage;
	private boolean suppressKickMessages;
	private ChatHandler chatHandler;
}
