package no.runsafe.nchat.command;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.PlayerArgument;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.player.IAmbiguousPlayer;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.PlayerChatEngine;

import javax.annotation.Nullable;
import java.util.Map;

public class PuppetCommand extends ExecutableCommand
{
	public PuppetCommand(PlayerChatEngine chatEngine, IServer server)
	{
		super("puppet", "Make it look like someone said something", "runsafe.nchat.puppet", new PlayerArgument(), new TrailingArgument("message"));
		this.chatEngine = chatEngine;
		this.server = server;
	}

	@Nullable
	@Override
	public String OnExecute(ICommandExecutor executor, Map<String, String> parameters)
	{
		IPlayer targetPlayer = server.getPlayer(parameters.get("player"));
		if (targetPlayer == null)
			return "&cThat player does not exist.";

		if (targetPlayer instanceof IAmbiguousPlayer)
			return targetPlayer.toString();

		chatEngine.broadcastMessageAsPlayer(targetPlayer, parameters.get("message"));
		return null;
	}

	private final PlayerChatEngine chatEngine;
	private final IServer server;
}
