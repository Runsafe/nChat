package no.runsafe.nchat.events;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerQuitEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerQuitEvent;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;
import no.runsafe.nchat.handlers.WhisperHandler;

public class LeaveEvent implements IPlayerQuitEvent, IConfigurationChanged
{
	public LeaveEvent(ChatHandler chatHandler, WhisperHandler whisperHandler)
	{
		this.chatHandler = chatHandler;
		this.whisperHandler = whisperHandler;
	}

	@Override
	public void OnPlayerQuit(RunsafePlayerQuitEvent runsafePlayerQuitEvent)
	{
		RunsafePlayer player = runsafePlayerQuitEvent.getPlayer();
		String message = player.getName().equals("sarahbakescakes") ? "was teleported to Kruithne's house." : this.leaveServerMessage;
		runsafePlayerQuitEvent.setQuitMessage(this.chatHandler.formatPlayerSystemMessage(message, player));
		this.whisperHandler.deleteLastWhisperedBy(player);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.leaveServerMessage = iConfiguration.getConfigValueAsString("chatMessage.leaveServer");
	}

	private final ChatHandler chatHandler;
	private final WhisperHandler whisperHandler;
	private String leaveServerMessage;
}
