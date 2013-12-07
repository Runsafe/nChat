package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.PlayerArgument;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafeAmbiguousPlayer;
import no.runsafe.nchat.chat.ChatEngine;

import java.util.Map;

public class PuppetCommand extends ExecutableCommand
{
	public PuppetCommand(ChatEngine chatEngine)
	{
		super("puppet", "Make it look like someone said something", "runsafe.nchat.puppet", new PlayerArgument(), new TrailingArgument("message"));
		this.chatEngine = chatEngine;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, Map<String, String> args)
	{
		IPlayer targetPlayer = RunsafeServer.Instance.getPlayer(args.get("player"));
		if (targetPlayer == null)
			return "&cThat player does not exist.";

		if (targetPlayer instanceof RunsafeAmbiguousPlayer)
			return targetPlayer.toString();

		chatEngine.broadcastMessageAsPlayer(targetPlayer, args.get("message"));
		return null;
	}

	private ChatEngine chatEngine;
}
