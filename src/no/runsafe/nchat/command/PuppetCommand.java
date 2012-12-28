package no.runsafe.nchat.command;

import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;

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
		if (args.length == 2)
		{
			RunsafePlayer targetPlayer = RunsafeServer.Instance.getPlayer(args[0]);
			RunsafeServer.Instance.broadcastMessage(this.chatHandler.formatChatMessage(args[1],targetPlayer));
		}
		return false;
	}

	private ChatHandler chatHandler;
}
