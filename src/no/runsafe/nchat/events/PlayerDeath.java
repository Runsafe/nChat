package no.runsafe.nchat.events;

import no.runsafe.framework.event.player.IPlayerDeathEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerDeathEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;

public class PlayerDeath implements IPlayerDeathEvent
{
	public PlayerDeath(ChatHandler chatHandler)
	{
		this.chatHandler = chatHandler;
	}

	@Override
	public void OnPlayerDeathEvent(RunsafePlayerDeathEvent runsafePlayerDeathEvent)
	{
		// TODO: Work out how they died and allow message modification.
		String originalMessage = runsafePlayerDeathEvent.getDeathMessage();
		RunsafePlayer player = (RunsafePlayer) runsafePlayerDeathEvent.getEntity();
		runsafePlayerDeathEvent.setDeathMessage(this.chatHandler.formatPlayerSystemMessage(originalMessage, player));
	}

	private ChatHandler chatHandler;
}
