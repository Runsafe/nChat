package no.runsafe.nchat.chat;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.channel.ChannelManager;
import no.runsafe.nchat.channel.IChatChannel;

import javax.annotation.Nullable;
import java.util.HashMap;

public class WhisperHandler
{
	public WhisperHandler(IServer server, ChannelManager manager)
	{
		this.server = server;
		this.manager = manager;
		lastWhisperList = new HashMap<String, String>(0);
	}

	public void sendWhisper(ICommandExecutor sender, ICommandExecutor target, String message)
	{
		IChatChannel channel = manager.getPrivateChannel(sender, target);
		InternalChatEvent event = new InternalChatEvent(sender instanceof IPlayer ? (IPlayer) sender : null, message, null);
		manager.setDefaultChannel(sender, channel);
		channel.Send(event);
		setLastWhisperedBy(target, sender);
	}

	private void setLastWhisperedBy(ICommandExecutor player, ICommandExecutor whisperer)
	{
		lastWhisperList.put(player.getName(), whisperer.getName());
	}

	public void deleteLastWhisperedBy(ICommandExecutor player)
	{
		if (lastWhisperList.containsKey(player.getName()))
			lastWhisperList.remove(player.getName());
	}

	@Nullable
	public IPlayer getLastWhisperedBy(ICommandExecutor player)
	{
		String playerName = player.getName();

		if (lastWhisperList.containsKey(playerName))
		{
			IPlayer whisperer = server.getPlayer(lastWhisperList.get(playerName));
			if (whisperer != null)
				return whisperer;
		}
		return null;
	}

	private final HashMap<String, String> lastWhisperList;
	private final IServer server;
	private final ChannelManager manager;
}
