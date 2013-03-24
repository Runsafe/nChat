package no.runsafe.nchat.command;

import no.runsafe.framework.command.player.PlayerCommand;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.handlers.MuteHandler;

import java.util.HashMap;

public class MuteCommand extends PlayerCommand
{

	public MuteCommand(IOutput console, MuteHandler muteHandler)
	{
		super("mute", "Suppress chat messages from a player", "runsafe.nchat.mute", "player");
		this.console = console;
		this.muteHandler = muteHandler;
	}

	@Override
	public String OnExecute(RunsafePlayer player, HashMap<String, String> args)
	{
		String mutePlayerName = args.get("player");

		if (mutePlayerName.equalsIgnoreCase("server"))
		{
			if (!player.hasPermission("runsafe.nchat.mute.server"))
				return Constants.COMMAND_NO_PERMISSION;

			this.muteHandler.muteServer();
			console.write(String.format("%s muted server chat.", player.getName()));
			return Constants.DEFAULT_MESSAGE_COLOR + Constants.COMMAND_CHAT_MUTED;
		}
		RunsafePlayer mutePlayer = RunsafeServer.Instance.getPlayer(mutePlayerName);

		if (mutePlayer == null)
			player.sendMessage(Constants.COMMAND_TARGET_NO_EXISTS);

		if (mutePlayer.hasPermission("runsafe.nchat.mute.exempt"))
			return Constants.COMMAND_TARGET_EXEMPT;

		console.write(String.format(
			"%s muted %s",
			player.getName(),
			mutePlayer.getName()
		));
		this.muteHandler.mutePlayer(mutePlayer);
		return Constants.DEFAULT_MESSAGE_COLOR + "Muted " + mutePlayerName;
	}

	private final IOutput console;
	private final MuteHandler muteHandler;
}
