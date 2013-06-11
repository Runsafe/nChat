package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerCustomEvent;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;

public class VanishEvent implements IPlayerCustomEvent
{
	public VanishEvent(ChatHandler chatHandler)
	{
		this.chatHandler = chatHandler;
	}

	@Override
	public void OnPlayerCustomEvent(RunsafePlayer runsafePlayer, String s, Object o)
	{
		if (s.equalsIgnoreCase("vanished"))
			this.chatHandler.refreshPlayerTabListName(runsafePlayer);
	}

	private final ChatHandler chatHandler;
}
