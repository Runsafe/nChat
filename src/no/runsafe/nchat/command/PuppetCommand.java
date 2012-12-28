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
		super("puppet");
		this.chatHandler = chatHandler;
	}

	@Override
	public boolean Execute(RunsafePlayer player, String[] args)
	{
		if (player.hasPermission("runsafe.nchat.puppet"))
		{
			if (args.length > 1)
			{
				RunsafePlayer targetPlayer = RunsafeServer.Instance.getPlayer(args[0]);

				if (targetPlayer != null)
				{
					String message = StringUtils.join(args, " ", 1, args.length);
					RunsafeServer.Instance.broadcastMessage(this.chatHandler.formatChatMessage(message ,targetPlayer));
				}
				else
				{
					player.sendMessage(Constants.COMMAND_TARGET_NO_EXISTS);
				}
				return true;
			}
		}
		else
		{
			player.sendMessage(Constants.COMMAND_NO_PERMISSION);
			return true;
		}
		return false;
	}

	private ChatHandler chatHandler;
}
