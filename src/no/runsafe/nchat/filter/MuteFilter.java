package no.runsafe.nchat.filter;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.MuteHandler;

import javax.annotation.Nullable;

public class MuteFilter implements ISpamFilter
{
	public MuteFilter(MuteHandler handler)
	{
		this.handler = handler;
	}

	@Nullable
	@Override
	public String processString(IPlayer player, String message)
	{
		if (handler.isPlayerMuted(player))
		{
			if (handler.isShadowMuted(player))
				player.sendColouredMessage(message);

			return null;
		}
		return message;
	}

	private final MuteHandler handler;
}
