package no.runsafe.nchat.events;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.player.IPlayerKickEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerKickEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;
import no.runsafe.nchat.handlers.SpamHandler;
import no.runsafe.nchat.handlers.WhisperHandler;

public class KickEvent implements IPlayerKickEvent, IConfigurationChanged
{
	public KickEvent(ChatHandler chatHandler, WhisperHandler whisperHandler, SpamHandler spamHandler)
	{
		this.chatHandler = chatHandler;
		this.whisperHandler = whisperHandler;
		this.spamHandler = spamHandler;
	}

	@Override
	public void OnPlayerKick(RunsafePlayerKickEvent runsafePlayerKickEvent)
	{
		RunsafePlayer kickedPlayer = runsafePlayerKickEvent.getPlayer();
		runsafePlayerKickEvent.setLeaveMessage(
			(this.suppressKickMessages)
				? null
				: this.chatHandler.formatPlayerSystemMessage(
						this.kickMessage.replace("#reason", runsafePlayerKickEvent.getReason()),
						kickedPlayer
					)
		);
		this.spamHandler.flushPlayer(kickedPlayer);
		this.whisperHandler.deleteLastWhisperedBy(kickedPlayer);
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
	private WhisperHandler whisperHandler;
	private SpamHandler spamHandler;
}
