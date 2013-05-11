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
		if (suppressKickMessages)
			runsafePlayerKickEvent.setLeaveMessage(null);
		RunsafePlayer kickedPlayer = runsafePlayerKickEvent.getPlayer();
		String format = kickedPlayer.isBanned() ? banMessage : kickMessage;
		runsafePlayerKickEvent.setLeaveMessage(
			this.chatHandler.formatPlayerSystemMessage(
				format.replace("#reason", runsafePlayerKickEvent.getReason()),
				kickedPlayer
			)
		);
		this.spamHandler.flushPlayer(kickedPlayer);
		this.whisperHandler.deleteLastWhisperedBy(kickedPlayer);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.kickMessage = configuration.getConfigValueAsString("chatMessage.kicked");
		this.banMessage = configuration.getConfigValueAsString("chatMessage.banned");
		this.suppressKickMessages = configuration.getConfigValueAsBoolean("spamControl.suppressKickMessages");
	}

	private String kickMessage;
	private String banMessage;
	private boolean suppressKickMessages;
	private final ChatHandler chatHandler;
	private final WhisperHandler whisperHandler;
	private final SpamHandler spamHandler;
}
