package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.Player;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.MuteHandler;

public class UnMuteCommand extends ExecutableCommand
{
	public UnMuteCommand(IConsole console, MuteHandler muteHandler)
	{
		super(
			"unmute", "Unmutes a previously muted player", "runsafe.nchat.mute",
			new Player.Any().require()
		);
		this.console = console;
		this.muteHandler = muteHandler;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		IPlayer player = executor instanceof IPlayer ? (IPlayer) executor : null;
		String unMutePlayerName = parameters.get("player");

		if (unMutePlayerName.equalsIgnoreCase("server"))
		{
			if (player == null || player.hasPermission("nChat.commands.muteServer"))
			{
				muteHandler.unMuteServer();
				return "&bGlobal chat has been un-muted! Praise the sun.";
			}
			else
			{
				return "&cYou do not have permission to do that.";
			}
		}
		if (player != null && !player.hasPermission("nChat.commands.mutePlayer"))
			return "&cYou do not have permission to do that.";

		IPlayer unMutePlayer = parameters.getValue("player");

		if (unMutePlayer == null)
			return "&cTry to pick a player who exists.";

		if (player != null && unMutePlayer.hasPermission("nChat.muteExempt"))
			return "&cThat player is exempt from being un-muted, silly as it sounds."; // Unless you are the console ^w^

		console.logInformation(String.format("%s un-muted %s", executor.getName(), unMutePlayer.getName()));
		muteHandler.unMutePlayer(unMutePlayer);
		return String.format("&bUnmuted %s.", unMutePlayer.getPrettyName());
	}

	private final IConsole console;
	private final MuteHandler muteHandler;
}
