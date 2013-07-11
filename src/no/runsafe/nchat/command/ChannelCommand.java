package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.handlers.ChatChannelHandler;

import java.util.HashMap;

public class ChannelCommand extends PlayerCommand
{
	public ChannelCommand(ChatChannelHandler chatChannelHandler)
	{
		super("channel", "Sends a message to a channel", null, "channel", "message");
		this.chatChannelHandler = chatChannelHandler;
		captureTail();
	}

	@Override
	public String OnExecute(RunsafePlayer player, HashMap<String, String> args)
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
