package no.runsafe.nchat.chat;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerFakeChatEvent;
import no.runsafe.nchat.channel.IChatChannel;

public class EmoteEvent extends InternalChatEvent
{
	public EmoteEvent(IChatChannel channel, IPlayer player, String message, IPlayer targetPlayer, String emote)
	{
		super(player, message, channel);
		this.emote = emote;
		target = targetPlayer;
	}

	public String getEmote()
	{
		if (target == null)
			return String.format(emote, getPlayer().getPrettyName());
		else
			return String.format(emote, getPlayer().getPrettyName(), target.getPrettyName());
	}

	private final String emote;
	private final IPlayer target;
}
