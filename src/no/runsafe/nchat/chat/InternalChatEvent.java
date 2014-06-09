package no.runsafe.nchat.chat;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerFakeChatEvent;
import no.runsafe.nchat.channel.IChatChannel;

public class InternalChatEvent extends RunsafePlayerFakeChatEvent
{
	public InternalChatEvent(IPlayer player, String message, IChatChannel channel)
	{
		super(player, message);
		this.channel = channel;
	}

	public IChatChannel getChannel()
	{
		return channel;
	}

	private final IChatChannel channel;
}
