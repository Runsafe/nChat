package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerChatEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerChatEvent;
import no.runsafe.nchat.channel.ChannelManager;
import no.runsafe.nchat.chat.InternalChatEvent;

public class ChatEvent implements IPlayerChatEvent
{
	public ChatEvent(ChannelManager channelManager)
	{
		this.channelManager = channelManager;
	}

	@Override
	public void OnPlayerChatEvent(RunsafePlayerChatEvent event)
	{
		// Don't go into recursion on our own internal events...
		if (event instanceof InternalChatEvent)
			return;

		event.cancel(); // We don't want Minecraft handling this.
		channelManager.getDefaultChannel(event.getPlayer()).Send(event.getPlayer(), event.getMessage());
	}

	private final ChannelManager channelManager;
}
