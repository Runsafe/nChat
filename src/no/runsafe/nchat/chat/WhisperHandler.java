package no.runsafe.nchat.chat;

import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.player.RunsafeFakePlayer;
import no.runsafe.nchat.channel.ChannelManager;
import no.runsafe.nchat.channel.IChatChannel;

import javax.annotation.Nullable;
import java.util.HashMap;

public class WhisperHandler
{
	public WhisperHandler(ChannelManager manager)
	{
		this.manager = manager;
		lastWhisperList = new HashMap<>(0);
	}

	public void sendWhisper(ICommandExecutor sender, ICommandExecutor target, String message)
	{
		IChatChannel channel = manager.getPrivateChannel(sender, target);
		if (channel == null)
			return;

		sendWhisper(sender, channel, message);
		setLastWhisperedBy(target, channel);
	}

	public void sendWhisper(ICommandExecutor sender, IChatChannel channel, String message)
	{
		InternalChatEvent event = new InternalChatEvent(
			sender instanceof IPlayer ? (IPlayer) sender : new RunsafeFakePlayer(sender.getName()),
			message,
			channel
		);
		manager.setDefaultChannel(sender, channel);
		channel.Send(event);
	}

	private void setLastWhisperedBy(ICommandExecutor player, IChatChannel channel)
	{
		lastWhisperList.put(player.getName(), channel);
	}

	public void deleteLastWhisperedBy(ICommandExecutor player)
	{
		if (lastWhisperList.containsKey(player.getName()))
			lastWhisperList.remove(player.getName());
	}

	@Nullable
	public IChatChannel getLastWhisperedBy(String playerName)
	{
		if (lastWhisperList.containsKey(playerName))
			return lastWhisperList.get(playerName);
		return null;
	}

	private final HashMap<String, IChatChannel> lastWhisperList;
	private final ChannelManager manager;
}
