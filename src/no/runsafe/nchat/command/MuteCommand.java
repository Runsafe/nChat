package no.runsafe.nchat.command;

import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.command.argument.PlayerArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.nchat.chat.MuteHandler;

import java.util.Map;

public class MuteCommand extends PlayerCommand
{

	public MuteCommand(IOutput console, MuteHandler muteHandler)
	{
		super(
			"mute", "Suppress chat messages from a player", "runsafe.nchat.mute",
			new PlayerArgument()
		);
		this.console = console;
		this.muteHandler = muteHandler;
	}

	@Override
	public String OnExecute(IPlayer player, Map<String, String> args)
	{
		String mutePlayerName = args.get("player");

		if (mutePlayerName.equalsIgnoreCase("server"))
		{
			if (!player.hasPermission("runsafe.nchat.mute.server"))
				return "&cYou do not have permission to do that";

			this.muteHandler.muteServer();
			return "&bGlobal chat has been muted, you monster.";
		}
		IPlayer mutePlayer = RunsafeServer.Instance.getPlayer(mutePlayerName);

		if (mutePlayer == null)
			return "&cThat player does not exist.";

		if (mutePlayer.hasPermission("runsafe.nchat.mute.exempt"))
			return "&cNice try, but you cannot mute that player.";

		console.logInformation(String.format("%s muted %s", player.getName(), mutePlayer.getName()));
		this.muteHandler.mutePlayer(mutePlayer);
		return String.format("&bMuted %s.", mutePlayer.getPrettyName());
	}

	private final IOutput console;
	private final MuteHandler muteHandler;
}
