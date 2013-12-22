package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerChatEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerChatEvent;
import no.runsafe.nchat.chat.PlayerChatEngine;
import no.runsafe.nchat.chat.InternalChatEvent;

public class ChatEvent implements IPlayerChatEvent
{
	public ChatEvent(PlayerChatEngine chatEngine)
	{
		this.chatEngine = chatEngine;
	}

	@Override
	public void OnPlayerChatEvent(RunsafePlayerChatEvent event)
	{
		// Don't go into recursion on our own internal events...
		if (event instanceof InternalChatEvent)
			return;

		event.cancel(); // We don't want Minecraft handling this.
		chatEngine.playerBroadcast(event.getPlayer(), event.getMessage());
	}

	private final PlayerChatEngine chatEngine;
}
