package no.runsafe.nchat.command;

import no.runsafe.framework.api.IServer;
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

	@Override
	public String OnExecute(IPlayer player, Map<String, String> args)
	{
		IPlayer ignorePlayer = server.getPlayerExact(args.get("player"));
		if (ignorePlayer == null)
			return "&cUnable to ignore that player, server error.";

		if (ignoreHandler.playerIsIgnoring(player, ignorePlayer))
		{
			ignoreHandler.removeIgnorePlayer(player, ignorePlayer);
			return String.format("&aYou are no longer ignoring %s.", ignorePlayer.getName());
		}
		else
		{
			if (!ignoreHandler.canIgnore(ignorePlayer))
				return "&cYou cannot ignore that player.";

			ignoreHandler.ignorePlayer(player, ignorePlayer);
			return String.format("&aYou are now ignoring %s. Repeat the command again to un-ignore.", ignorePlayer.getName());
		}
	}

	private final IgnoreHandler ignoreHandler;
	private final IServer server;
}
