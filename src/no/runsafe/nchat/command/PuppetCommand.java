package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.internal.command.ExecutableCommand;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerFakeChatEvent;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.Constants;

import java.util.HashMap;

public class PuppetCommand extends ExecutableCommand
{
	public PuppetCommand()
	{
		super("puppet", "Make it look like someone said something", "runsafe.nchat.puppet", "player", "message");
		captureTail();
	}

	@Override
	public String OnExecute(ICommandExecutor executor, HashMap<String, String> args)
	{
		RunsafePlayer targetPlayer = RunsafeServer.Instance.getPlayer(args.get("player"));
		if (targetPlayer == null)
			return Constants.COMMAND_TARGET_NO_EXISTS;

		RunsafePlayerFakeChatEvent chat = new RunsafePlayerFakeChatEvent(targetPlayer, args.get("message"));
		chat.Fire();
		RunsafeServer.Instance.broadcastMessage(String.format(chat.getFormat(), chat.getMessage()));
		return null;
	}
}
