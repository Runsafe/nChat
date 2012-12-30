package no.runsafe.nchat.command;

import no.runsafe.framework.command.RunsafePlayerCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;
import org.apache.commons.lang.StringUtils;

public class EmoteCommand extends RunsafePlayerCommand
{
	public EmoteCommand(ChatHandler chatHandler)
	{
		super("me", "message");
		this.chatHandler = chatHandler;
	}

	@Override
	public String OnExecute(RunsafePlayer executor, String[] args)
	{
		String message = StringUtils.join(args, " ", 0, args.length);
		RunsafeServer.Instance.broadcastMessage(this.chatHandler.formatPlayerSystemMessage(message, executor));
		return null;
	}

	@Override
	public String requiredPermission()
	{
		return "runsafe.nchat.emote";
	}

	private ChatHandler chatHandler;
}
