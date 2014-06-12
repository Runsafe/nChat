package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.minecraft.RunsafeConsole;
import no.runsafe.nchat.channel.GlobalChatChannel;
import no.runsafe.nchat.channel.IChannelManager;
import no.runsafe.nchat.channel.IChatChannel;

public class SayCommand extends ExecutableCommand
{
	public SayCommand(IConsole console, IChannelManager manager)
	{
		super("say", "Make the server say some words of wisdom", "runsafe.console.say", new TrailingArgument("message"));
		this.console = new RunsafeConsole(console);
		this.manager = manager;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		String message = parameters.getValue("message");
		IChatChannel channel = manager.getChannelByName(GlobalChatChannel.CHANNELNAME);
		channel.SendSystem(manager.FormatMessage(console, channel, message));
		return null;
	}

	private final ICommandExecutor console;
	private final IChannelManager manager;
}
