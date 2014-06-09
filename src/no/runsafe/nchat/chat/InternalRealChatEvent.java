package no.runsafe.nchat.chat;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerFakeChatEvent;

public class InternalRealChatEvent extends RunsafePlayerFakeChatEvent
{
	public InternalRealChatEvent(IPlayer player, String message)
	{
		super(player, message);
	}

	@Override
	public boolean isFake()
	{
		return false;
	}
}
