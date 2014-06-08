package no.runsafe.nchat.filter;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.IgnoreHandler;

import javax.annotation.Nullable;

public class IgnoreFilter implements IChatFilter
{
	public IgnoreFilter(IgnoreHandler handler)
	{
		this.handler = handler;
	}

	@Nullable
	@Override
	public String processString(IPlayer source, IPlayer target, String message)
	{
		return handler.playerIsIgnoring(target, source) ? null : message;
	}

	private final IgnoreHandler handler;
}
