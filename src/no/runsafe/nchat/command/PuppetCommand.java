package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.PlayerArgument;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerFakeChatEvent;
import no.runsafe.framework.minecraft.player.RunsafeAmbiguousPlayer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.Constants;

import java.util.Map;

public class PuppetCommand extends ExecutableCommand
{
	public PuppetCommand()
	{
		super("puppet", "Make it look like someone said something", "runsafe.nchat.puppet", new PlayerArgument(), new TrailingArgument("message"));
	}

	@Override
	public String OnExecute(ICommandExecutor executor, Map<String, String> args)
	{
		RunsafePlayer targetPlayer = RunsafeServer.Instance.getPlayer(args.get("player"));
		if (targetPlayer == null)
			return Constants.COMMAND_TARGET_NO_EXISTS;

		if (targetPlayer instanceof RunsafeAmbiguousPlayer)
			return targetPlayer.toString();

		RunsafePlayerFakeChatEvent.Broadcast(targetPlayer, args.get("message"));
		return null;
	}
}
