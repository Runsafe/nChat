package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerChatEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerChatEvent;
import no.runsafe.nchat.chat.ChatEngine;

public class ChatEvent implements IPlayerChatEvent
{
	public ChatEvent(ChatEngine chatEngine)
	{
		this.chatEngine = chatEngine;
	}

	@Override
	public void OnPlayerChatEvent(RunsafePlayerChatEvent event)
	{
		if (!event.isFake())
		{
			event.cancel(); // We don't want Minecraft handling this.
			chatEngine.playerBroadcast(event.getPlayer(), event.getMessage());
		}
	}

	private final ChatEngine chatEngine;
}
