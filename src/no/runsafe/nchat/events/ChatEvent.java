package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerChatEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerChatEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerFakeChatEvent;
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
		if (!event.isFake())
			event.cancel(); // We don't want Minecraft handling this.

		channelManager.getDefaultChannel(event.getPlayer()).Send(event);
	}

	private final ChannelManager channelManager;
}
