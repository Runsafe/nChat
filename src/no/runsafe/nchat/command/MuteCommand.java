package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.BooleanArgument;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.Period;
import no.runsafe.framework.api.command.argument.Player;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.MuteHandler;


public class MuteCommand extends ExecutableCommand
{
	public MuteCommand(IConsole console, MuteHandler muteHandler)
	{
		super(
			"mute", "Suppress chat messages from a player", "runsafe.nchat.mute",
			new Player().require(), new BooleanArgument("shadow"), new Period()
		);
		this.console = console;
		this.muteHandler = muteHandler;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		IPlayer player = executor instanceof IPlayer ? (IPlayer) executor : null;
		IPlayer mutePlayer = parameters.getRequired("player");
		org.joda.time.Period duration = parameters.getValue("duration");

		if (player != null && mutePlayer.hasPermission("runsafe.nchat.mute.exempt"))
			return "&cNice try, but you cannot mute that player."; // Unless you are the console ^w^

		boolean isShadow = (Boolean) parameters.getRequired("shadow");
		String isShadowText = isShadow ? "shadow-" : "";
		console.logInformation(String.format("%s %smuted %s", executor.getName(), isShadowText, mutePlayer.getName()));

		if (duration != null)
			muteHandler.tempMutePlayer(mutePlayer, duration, isShadow);
		else
			muteHandler.mutePlayer(mutePlayer.getName(), isShadow);

		return String.format("&bYou %smuted %s.", isShadowText, mutePlayer.getPrettyName());
	}

	private final IConsole console;
	private final MuteHandler muteHandler;
}
