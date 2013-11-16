package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.Constants;

import java.util.Map;

public class ChannelCommand extends PlayerCommand
{
	public ChannelCommand(ChatChannelHandler chatChannelHandler)
	{
		super(
			"channel", "Sends a message to a channel", null,
			new RequiredArgument("channel"), new TrailingArgument("message")
		);
		this.chatChannelHandler = chatChannelHandler;
	}

	@Override
	public String OnExecute(RunsafePlayer player, Map<String, String> args)
	{
		String channelName = args.get("channel");
		String message = args.get("message");

		if (this.chatChannelHandler.isChannelInvalid(channelName))
			return Constants.DEFAULT_MESSAGE_COLOR + Constants.CHANNEL_NOT_EXIST;

		if (this.chatChannelHandler.blockChannelMessage(channelName, player))
			return Constants.DEFAULT_MESSAGE_COLOR + Constants.CHANNEL_NO_PERMISSION;

		this.chatChannelHandler.broadcastMessage(channelName, message, player);
		return null;
	}

	private final ChatChannelHandler chatChannelHandler;
}
