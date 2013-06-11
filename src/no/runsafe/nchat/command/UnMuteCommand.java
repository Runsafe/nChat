package no.runsafe.nchat.command;

import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.handlers.MuteHandler;

import java.util.HashMap;

public class UnMuteCommand extends PlayerCommand
{
	public UnMuteCommand(IOutput console, MuteHandler muteHandler)
	{
		super("unmute", "Unmutes a previously muted player", "runsafe.nchat.mute", "player");
		this.console = console;
		this.muteHandler = muteHandler;
	}

	@Override
	public String OnExecute(RunsafePlayer player, HashMap<String, String> args)
	{
		String unMutePlayerName = args.get("player");

		if (unMutePlayerName.equalsIgnoreCase("server"))
		{
			if (player.hasPermission("nChat.commands.muteServer"))
			{
				this.muteHandler.unMuteServer();
				console.write(String.format("%s un-muted server chat.", player.getName()));
				return Constants.DEFAULT_MESSAGE_COLOR + Constants.COMMAND_CHAT_UNMUTED;
			}
			else
				return Constants.COMMAND_NO_PERMISSION;
		}
		if (!player.hasPermission("nChat.commands.mutePlayer"))
			return Constants.COMMAND_NO_PERMISSION;

		RunsafePlayer unMutePlayer = RunsafeServer.Instance.getPlayer(unMutePlayerName);

		if (unMutePlayer == null)
			return Constants.COMMAND_TARGET_NO_EXISTS;

		if (unMutePlayer.hasPermission("nChat.muteExempt"))
			return Constants.COMMAND_TARGET_EXEMPT;

		console.write(String.format(
			"%s un-muted %s",
			player.getName(),
			unMutePlayer.getName()
		));
		this.muteHandler.unMutePlayer(unMutePlayer);
		return Constants.DEFAULT_MESSAGE_COLOR + "Unmuted " + unMutePlayer.getPrettyName();
	}

	private final IOutput console;
	private final MuteHandler muteHandler;
}
