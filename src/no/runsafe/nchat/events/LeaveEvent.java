package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerQuitEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerQuitEvent;
import no.runsafe.nchat.channel.IChannelManager;
import no.runsafe.nchat.chat.WhisperHandler;

public class LeaveEvent implements IPlayerQuitEvent
{
	public LeaveEvent(WhisperHandler whisperHandler, IChannelManager manager)
	{
		this.whisperHandler = whisperHandler;
		this.manager = manager;
	}

	@Override
	public void OnPlayerQuit(RunsafePlayerQuitEvent event)
	{
		event.setQuitMessage(null);
		IPlayer player = event.getPlayer();
		whisperHandler.deleteLastWhisperedBy(player);
		manager.closePrivateChannels(player);
	}

	private final WhisperHandler whisperHandler;
	private final IChannelManager manager;
}
