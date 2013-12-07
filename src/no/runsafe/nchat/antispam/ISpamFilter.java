package no.runsafe.nchat.antispam;

import no.runsafe.framework.api.player.IPlayer;

public interface ISpamFilter
{
	public String processString(IPlayer player, String message);
}
