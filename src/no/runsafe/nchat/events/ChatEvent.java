package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerChatEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerChatEvent;
import no.runsafe.nchat.channel.ChannelManager;
import no.runsafe.nchat.channel.IChatChannel;
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

		if (event instanceof InternalChatEvent)
		{
			IChatChannel channel = ((InternalChatEvent) event).getChannel();
			if(channel != null)
			{
				channel.Send(event);
				return;
			}
		}

		channelManager.getDefaultChannel(event.getPlayer()).Send(event);
	}

	private final ChannelManager channelManager;
}
