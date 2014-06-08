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
		return handler.isPlayerMuted(player) ? null : message;
	}

	private final MuteHandler handler;
}
