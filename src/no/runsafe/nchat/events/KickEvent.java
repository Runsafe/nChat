package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerKickEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerKickEvent;
import no.runsafe.nchat.channel.IChannelManager;
import no.runsafe.nchat.chat.WhisperHandler;

public class KickEvent implements IPlayerKickEvent
{
	public KickEvent(WhisperHandler whisperHandler, IChannelManager manager)
	{
		this.whisperHandler = whisperHandler;
		this.manager = manager;
	}

	@Override
	public void OnPlayerKick(RunsafePlayerKickEvent event)
	{
		IPlayer kickedPlayer = event.getPlayer();
		whisperHandler.deleteLastWhisperedBy(kickedPlayer);
		manager.closePrivateChannels(event.getPlayer());
	}

	private final WhisperHandler whisperHandler;
	private final IChannelManager manager;
}
