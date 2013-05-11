package no.runsafe.nchat.events;

import no.runsafe.framework.event.player.IPlayerKickEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerKickEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.handlers.SpamHandler;
import no.runsafe.nchat.handlers.WhisperHandler;

public class KickEvent implements IPlayerKickEvent
{
	public KickEvent(WhisperHandler whisperHandler, SpamHandler spamHandler)
	{
		this.whisperHandler = whisperHandler;
		this.spamHandler = spamHandler;
	}

	@Override
	public void OnPlayerKick(RunsafePlayerKickEvent runsafePlayerKickEvent)
	{
		RunsafePlayer kickedPlayer = runsafePlayerKickEvent.getPlayer();
		this.spamHandler.flushPlayer(kickedPlayer);
		this.whisperHandler.deleteLastWhisperedBy(kickedPlayer);
	}

	private final WhisperHandler whisperHandler;
	private final SpamHandler spamHandler;
}
