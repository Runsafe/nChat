package no.runsafe.nchat.filter;

import no.runsafe.framework.api.player.IPlayer;

import javax.annotation.Nullable;

public interface IChatFilter
{
	@Nullable
	String processString(IPlayer source, IPlayer target, String message);
}
