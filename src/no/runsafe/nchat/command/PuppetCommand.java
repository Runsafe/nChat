package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.Player;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.channel.ChannelManager;
import no.runsafe.nchat.chat.InternalChatEvent;
import no.runsafe.nchat.emotes.EmoteHandler;

import javax.annotation.Nullable;

public class PuppetCommand extends ExecutableCommand
{
	public PuppetCommand(ChannelManager channelManager, EmoteHandler emoteHandler)
	{
		super("puppet", "Make it look like someone said something", "runsafe.nchat.puppet", new Player().require(), new TrailingArgument("message"));
		this.channelManager = channelManager;
		this.emoteHandler = emoteHandler;
	}

	@Nullable
	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		IPlayer targetPlayer = parameters.getValue("player");
		if (targetPlayer == null)
			return null;

		String message = parameters.get("message");
		if (message != null)
		{
			if (message.startsWith("/"))
				emoteHandler.executeEmote(targetPlayer, message);
			else
			{
				InternalChatEvent event = new InternalChatEvent(targetPlayer, message);
				event.Fire();
				channelManager.getDefaultChannel(event.getPlayer()).Send(event.getPlayer(), event.getMessage());
			}
		}
		return null;
	}

	private final EmoteHandler emoteHandler;
	private final ChannelManager channelManager;
}
