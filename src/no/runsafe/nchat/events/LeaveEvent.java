package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerQuitEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerQuitEvent;
import no.runsafe.nchat.chat.WhisperHandler;

public class LeaveEvent implements IPlayerQuitEvent
{
	public LeaveEvent(WhisperHandler whisperHandler)
	{
		this.whisperHandler = whisperHandler;
	}

	@Override
	public void OnPlayerQuit(RunsafePlayerQuitEvent event)
	{
		event.setQuitMessage(null);
		IPlayer player = event.getPlayer();
		whisperHandler.deleteLastWhisperedBy(player);
	}

	private final WhisperHandler whisperHandler;
}
