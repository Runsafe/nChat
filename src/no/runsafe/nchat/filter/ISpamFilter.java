package no.runsafe.nchat.filter;

import no.runsafe.framework.api.player.IPlayer;

import javax.annotation.Nullable;

public interface ISpamFilter
{
	@Nullable
	String processString(IPlayer player, String message);
}
