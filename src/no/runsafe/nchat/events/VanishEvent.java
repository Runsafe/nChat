package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerCustomEvent;
import no.runsafe.framework.minecraft.event.player.RunsafeCustomEvent;
import no.runsafe.nchat.handlers.ChatHandler;

public class VanishEvent implements IPlayerCustomEvent
{
	public VanishEvent(ChatHandler chatHandler)
	{
		this.chatHandler = chatHandler;
	}

	@Override
	public void OnPlayerCustomEvent(RunsafeCustomEvent event)
	{
		if (event.getEvent().equalsIgnoreCase("vanished"))
			this.chatHandler.refreshPlayerTabListName(event.getPlayer());
	}

	private final ChatHandler chatHandler;
}
