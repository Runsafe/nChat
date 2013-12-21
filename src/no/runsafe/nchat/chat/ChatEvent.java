package no.runsafe.nchat.chat;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerFakeChatEvent;

public class ChatEvent extends RunsafePlayerFakeChatEvent
{
	public ChatEvent(IPlayer player, String message)
	{
		super(player, message);
	}
}
