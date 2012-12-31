package no.runsafe.nchat.events;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.player.IPlayerQuitEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerQuitEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;
import no.runsafe.nchat.handlers.SpamHandler;
import no.runsafe.nchat.handlers.WhisperHandler;

public class LeaveEvent implements IPlayerQuitEvent, IConfigurationChanged
{
	public LeaveEvent(ChatHandler chatHandler, WhisperHandler whisperHandler, SpamHandler spamHandler)
	{
		this.chatHandler = chatHandler;
		this.whisperHandler = whisperHandler;
		this.spamHandler = spamHandler;
	}

	@Override
	public void OnPlayerQuit(RunsafePlayerQuitEvent runsafePlayerQuitEvent)
	{
		RunsafePlayer player = runsafePlayerQuitEvent.getPlayer();
		runsafePlayerQuitEvent.setQuitMessage(this.chatHandler.formatPlayerSystemMessage(this.leaveServerMessage, player));
		this.spamHandler.flushPlayer(player);
		this.whisperHandler.deleteLastWhisperedBy(player);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.leaveServerMessage = iConfiguration.getConfigValueAsString("chatMessage.leaveServer");
	}

	private ChatHandler chatHandler;
	private WhisperHandler whisperHandler;
	private SpamHandler spamHandler;
	private String leaveServerMessage;
}
