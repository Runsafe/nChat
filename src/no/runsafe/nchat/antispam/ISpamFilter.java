package no.runsafe.nchat.antispam;

import no.runsafe.framework.minecraft.player.RunsafePlayer;

public interface ISpamFilter
{
	public String processString(RunsafePlayer player, String message);
}
