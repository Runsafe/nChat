package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.argument.PlayerArgument;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.Map;

public class IgnoreCommand extends PlayerCommand
{
	public IgnoreCommand()
	{
		super("ignore", "Toggle the ignoring of a player.", "runsafe.nchat.ignore", new PlayerArgument());
	}

	@Override
	public String OnExecute(RunsafePlayer runsafePlayer, Map<String, String> stringStringMap)
	{

	}
}
