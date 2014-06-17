package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.Player;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.channel.ChannelArgument;
import no.runsafe.nchat.channel.IChannelManager;
import no.runsafe.nchat.channel.IChatChannel;
import no.runsafe.nchat.chat.InternalChatEvent;
import no.runsafe.nchat.emotes.EmoteHandler;

import javax.annotation.Nullable;

public class PuppetCommand extends ExecutableCommand
{
	public PuppetCommand(EmoteHandler emoteHandler, IChannelManager manager)
	{
		super("puppet", "Make it look like someone said something", "runsafe.nchat.puppet", new Player().require(), new ChannelArgument(manager), new TrailingArgument("message"));
		this.emoteHandler = emoteHandler;
	}

	@Nullable
	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		IPlayer targetPlayer = parameters.getValue("player");
		if (targetPlayer == null)
			return null;
		IChatChannel targetChannel = parameters.getValue("channel");
		if (targetChannel == null)
			return "Unknown channel!";

		String message = parameters.getRequired("message");
		if (message.startsWith("/"))
			emoteHandler.executeEmote(targetChannel, executor, targetPlayer, message);
		else
		{
			InternalChatEvent event = new InternalChatEvent(targetPlayer, message, targetChannel);
			event.Fire();
		}
		return null;
	}

	private final EmoteHandler emoteHandler;
}
