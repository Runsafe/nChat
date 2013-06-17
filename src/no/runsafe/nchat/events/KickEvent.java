package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerKickEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerKickEvent;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.handlers.WhisperHandler;

public class KickEvent implements IPlayerKickEvent
{
	public KickEvent(WhisperHandler whisperHandler)
	{
		this.whisperHandler = whisperHandler;
	}

	@Override
	public void OnPlayerKick(RunsafePlayerKickEvent runsafePlayerKickEvent)
	{
		RunsafePlayer kickedPlayer = runsafePlayerKickEvent.getPlayer();
		this.whisperHandler.deleteLastWhisperedBy(kickedPlayer);
	}

	private final WhisperHandler whisperHandler;
}
