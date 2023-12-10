package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.Duration;
import no.runsafe.framework.api.command.argument.Player;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.MuteHandler;

public class MuteCommand extends ExecutableCommand
{
	public MuteCommand(MuteHandler muteHandler)
	{
		super(
			"mute", "Suppress chat messages from a player", "runsafe.nchat.mute",
			new Player().require(), new Duration()
		);

		this.muteHandler = muteHandler;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		IPlayer player = executor instanceof IPlayer ? (IPlayer) executor : null;
		IPlayer mutePlayer = parameters.getRequired("player");

		java.time.Duration duration = parameters.getValue("duration");

		if (player != null && mutePlayer.hasPermission("runsafe.nchat.mute.exempt"))
			return "&cNice try, but you cannot mute that player."; // Unless you are the console ^w^

		if (duration != null)
			muteHandler.tempMutePlayer(mutePlayer, duration);
		else
			muteHandler.mutePlayer(mutePlayer);

		return String.format("&bYou muted %s.", mutePlayer.getPrettyName());
	}

	private final MuteHandler muteHandler;
}
