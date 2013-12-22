package no.runsafe.nchat.command;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.PlayerArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.IgnoreHandler;

import java.util.Map;

public class IgnoreCommand extends PlayerCommand
{
	public IgnoreCommand(IServer server, IgnoreHandler ignoreHandler)
	{
		super("ignore", "Toggle the ignoring of a player.", "runsafe.nchat.ignore", new PlayerArgument());
		this.server = server;
		this.ignoreHandler = ignoreHandler;
	}

	/**
	 * Checks to see if a player can be ignored.
	 *
	 * @param ignorePlayer The player to check.
	 * @return True if the player can be ignored, otherwise false.
	 */
	public static boolean isMuteExempt(ICommandExecutor ignorePlayer)
	{
		return ignorePlayer.hasPermission("runsafe.nchat.ignore.exempt");
	}

	@Override
	public String OnExecute(IPlayer executor, Map<String, String> parameters)
	{
		IPlayer ignorePlayer = server.getPlayerExact(parameters.get("player"));
		if (ignorePlayer == null)
			return "&cUnable to ignore that player, server error.";

		if (ignoreHandler.playerIsIgnoring(executor, ignorePlayer))
		{
			ignoreHandler.removeIgnorePlayer(executor, ignorePlayer);
			return String.format("&aYou are no longer ignoring %s.", ignorePlayer.getName());
		}
		else
		{
			if (isMuteExempt(ignorePlayer))
				return "&cYou cannot ignore that player.";

			ignoreHandler.ignorePlayer(executor, ignorePlayer);
			return String.format("&aYou are now ignoring %s. Repeat the command again to un-ignore.", ignorePlayer.getName());
		}
	}

	private final IgnoreHandler ignoreHandler;
	private final IServer server;
}
