package no.runsafe.nchat.chat;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerFakeChatEvent;

public class InternalChatEvent extends RunsafePlayerFakeChatEvent
{
	public InternalChatEvent(IPlayer player, String message)
	{
		super(player, message);
	}
}
