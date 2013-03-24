package no.runsafe.nchat.command;

import no.runsafe.framework.command.Command;
import no.runsafe.framework.command.ExecutableCommand;
import no.runsafe.framework.command.player.PlayerCommand;
import no.runsafe.framework.server.ICommandExecutor;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.handlers.ChatHandler;

import java.util.HashMap;

public class PuppetCommand extends ExecutableCommand
{
	public PuppetCommand(ChatHandler chatHandler)
	{
		super("puppet", "Make it look like someone said something", "runsafe.nchat.puppet", "player", "message");
		this.chatHandler = chatHandler;
		captureTail();
	}

	@Override
	public String OnExecute(ICommandExecutor executor, HashMap<String, String> args)
	{
		RunsafePlayer targetPlayer = RunsafeServer.Instance.getPlayer(args.get("player"));
		if (targetPlayer != null)
			RunsafeServer.Instance.broadcastMessage(this.chatHandler.formatChatMessage(args.get("message"), targetPlayer));
		else
			executor.sendMessage(Constants.COMMAND_TARGET_NO_EXISTS);
		return null;
	}

	private final ChatHandler chatHandler;
}
