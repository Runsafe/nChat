package no.runsafe.nchat.command;

import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.handlers.ChatHandler;
import org.apache.commons.lang.StringUtils;

public class PuppetCommand extends RunsafeCommand
{
	public PuppetCommand(ChatHandler chatHandler)
	{
		super("puppet", "player", "message");
		this.chatHandler = chatHandler;
	}

	@Override
	public String requiredPermission()
	{
		return "runsafe.nchat.puppet";
	}

	@Override
	public String OnExecute(RunsafePlayer executor, String[] args)
	{
		if (args.length > 1)
		{
			RunsafePlayer targetPlayer = RunsafeServer.Instance.getPlayer(args[0]);

			if (targetPlayer != null)
			{
				String message = StringUtils.join(args, " ", 1, args.length);
				RunsafeServer.Instance.broadcastMessage(this.chatHandler.formatChatMessage(message, targetPlayer));
			}
			else
			{
				executor.sendMessage(Constants.COMMAND_TARGET_NO_EXISTS);
			}
		}
		return null;
	}

	private ChatHandler chatHandler;
}
