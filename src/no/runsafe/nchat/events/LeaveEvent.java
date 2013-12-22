package no.runsafe.nchat.events;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerQuitEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerQuitEvent;
import no.runsafe.nchat.chat.PlayerChatEngine;
import no.runsafe.nchat.chat.WhisperHandler;

public class LeaveEvent implements IPlayerQuitEvent, IConfigurationChanged
{
	public LeaveEvent(PlayerChatEngine chatEngine, WhisperHandler whisperHandler)
	{
		this.chatEngine = chatEngine;
		this.whisperHandler = whisperHandler;
	}

	@Override
	public void OnPlayerQuit(RunsafePlayerQuitEvent event)
	{
		event.setQuitMessage(null);
		IPlayer player = event.getPlayer();
		chatEngine.broadcastMessage(leaveServerMessage.replace("#player", player.getPrettyName()));
		whisperHandler.deleteLastWhisperedBy(player);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		leaveServerMessage = configuration.getConfigValueAsString("chatMessage.leaveServer");
	}

	private final PlayerChatEngine chatEngine;
	private final WhisperHandler whisperHandler;
	private String leaveServerMessage;
}
