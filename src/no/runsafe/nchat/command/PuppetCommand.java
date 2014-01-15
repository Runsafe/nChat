package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.AnyPlayerRequired;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.player.IAmbiguousPlayer;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.PlayerChatEngine;

import javax.annotation.Nullable;

public class PuppetCommand extends ExecutableCommand
{
	public PuppetCommand(PlayerChatEngine chatEngine)
	{
		super("puppet", "Make it look like someone said something", "runsafe.nchat.puppet", new AnyPlayerRequired(), new TrailingArgument("message"));
		this.chatEngine = chatEngine;
	}

	@Nullable
	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		IPlayer targetPlayer = parameters.getPlayer("player");
		if (targetPlayer == null)
			return "&cThat player does not exist.";

		if (targetPlayer instanceof IAmbiguousPlayer)
			return targetPlayer.toString();

		chatEngine.broadcastMessageAsPlayer(targetPlayer, parameters.get("message"));
		return null;
	}

	private final PlayerChatEngine chatEngine;
}
