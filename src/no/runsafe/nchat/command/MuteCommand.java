package no.runsafe.nchat.command;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.PlayerArgument;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.MuteHandler;

import java.util.Map;

public class MuteCommand extends ExecutableCommand
{
	public MuteCommand(IServer server, IConsole console, MuteHandler muteHandler)
	{
		super(
			"mute", "Suppress chat messages from a player", "runsafe.nchat.mute",
			new PlayerArgument()
		);
		this.server = server;
		this.console = console;
		this.muteHandler = muteHandler;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, Map<String, String> parameters)
	{
		IPlayer player = executor instanceof IPlayer ? (IPlayer) executor : null;
		String mutePlayerName = parameters.get("player");

		if (mutePlayerName.equalsIgnoreCase("server"))
		{
			if (player != null && !player.hasPermission("runsafe.nchat.mute.server"))
				return "&cYou do not have permission to do that";

			muteHandler.muteServer();
			return "&bGlobal chat has been muted, you monster.";
		}
		IPlayer mutePlayer = server.getPlayer(mutePlayerName);

		if (mutePlayer == null)
			return "&cThat player does not exist.";

		if (player != null && mutePlayer.hasPermission("runsafe.nchat.mute.exempt"))
			return "&cNice try, but you cannot mute that player."; // Unless you are the console ^w^

		console.logInformation(String.format("%s muted %s", executor.getName(), mutePlayer.getName()));
		muteHandler.mutePlayer(mutePlayer);
		return String.format("&bMuted %s.", mutePlayer.getPrettyName());
	}

	private final IConsole console;
	private final MuteHandler muteHandler;
	private final IServer server;
}
