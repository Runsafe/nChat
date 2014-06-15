package no.runsafe.nchat.channel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.hook.IGlobalPluginAPI;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.IgnoreHandler;
import no.runsafe.nchat.chat.formatting.ChatFormatter;
import no.runsafe.nchat.filter.IChatFilter;
import no.runsafe.nchat.filter.ISpamFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelManager implements IChannelManager, IGlobalPluginAPI
{
	public ChannelManager(List<ISpamFilter> inboundFilters, List<IChatFilter> outboundFilters, IgnoreHandler ignoreHandler, IConsole console, ChatFormatter chatFormatter)
	{
		this.inboundFilters = inboundFilters;
		this.outboundFilters = outboundFilters;
		this.ignoreHandler = ignoreHandler;
		this.console = console;
		this.chatFormatter = chatFormatter;
	}

	@Override
	public void registerSpamFilter(ISpamFilter filter)
	{
		inboundFilters.add(filter);
	}

	@Override
	public void registerChatFilter(IChatFilter filter)
	{
		outboundFilters.add(filter);
	}

	@Override
	public IChatChannel getPrivateChannel(ICommandExecutor player1, ICommandExecutor player2)
	{
		String player1Name = player1.getName();
		String player2Name = player2.getName();
		int cmp = player1Name.compareToIgnoreCase(player2Name);
		if (cmp == 0)
			return null;

		if (ignoreHandler.playerIsIgnoring(player1Name, player2Name))
			return null;

		if (ignoreHandler.playerIsIgnoring(player2Name, player1Name))
			return null;

		String name = "%" + (cmp < 0 ? player1Name : player2Name) + "-" + (cmp > 0 ? player1Name : player2Name);
		if (!channels.containsKey(name))
		{
			IChatChannel channel = new PrivateChannel(this, console, name, player1, player2);
			channels.put(name, channel);
			registerPrivateChannel(player1, name);
			registerPrivateChannel(player2, name);
		}
		return channels.get(name);
	}

	private void registerPrivateChannel(ICommandExecutor player, String channel)
	{
		if (!privateChannels.containsKey(player.getName()))
			privateChannels.put(player.getName(), new ArrayList<String>(1));
		privateChannels.get(player.getName()).add(channel);
	}

	@Override
	public void closePrivateChannels(ICommandExecutor player)
	{
		if (privateChannels.containsKey(player.getName()))
		{
			for (String channel : privateChannels.get(player.getName()))
			{
				if (channels.containsKey(channel))
					channels.remove(channel);
			}
			privateChannels.remove(player.getName());
		}
	}

	@Override
	public String filter(ICommandExecutor player, String incoming)
	{
		if (!(player instanceof IPlayer))
			return incoming;

		String message = incoming;
		for (ISpamFilter filter : inboundFilters)
		{
			if (message == null || message.isEmpty())
				return null;
			message = filter.processString((IPlayer) player, message);
		}
		return message;
	}

	@Override
	public String filter(ICommandExecutor player, ICommandExecutor member, String incoming)
	{
		if (!(member instanceof IPlayer && player instanceof IPlayer))
			return incoming;

		String message = incoming;
		for (IChatFilter filter : outboundFilters)
		{
			if (message == null || message.isEmpty())
				return null;
			message = filter.processString((IPlayer) player, (IPlayer) member, message);
		}
		return message;
	}

	@Override
	public String FormatPrivateMessageLog(ICommandExecutor you, ICommandExecutor player, String message)
	{
		return chatFormatter.formatPrivateMessageLog(you, player, message);
	}

	@Override
	public String FormatPrivateMessageTo(ICommandExecutor you, ICommandExecutor player, String message)
	{
		return chatFormatter.formatPrivateMessageTo(you, player, message);
	}

	@Override
	public String FormatPrivateMessageFrom(ICommandExecutor you, ICommandExecutor player, String message)
	{
		return chatFormatter.formatPrivateMessageFrom(you, player, message);
	}

	@Override
	public String FormatMessage(ICommandExecutor player, IChatChannel channel, String message)
	{
		return chatFormatter.formatMessage(player, channel, message);
	}

	@Override
	public void addChannelToList(ICommandExecutor player, IChatChannel channel)
	{
		if (!channelLists.containsKey(player.getName()))
			channelLists.put(player.getName(), new ArrayList<IChatChannel>(1));
		channelLists.get(player.getName()).add(channel);
		int index = channelLists.get(player.getName()).indexOf(channel) + 1;
		player.sendColouredMessage("Joined channel %d. %s", index, channel.getName());
	}

	@Override
	public void removeChannelFromList(ICommandExecutor player, IChatChannel channel)
	{
		if (channelLists.containsKey(player.getName()) && channelLists.get(player.getName()).contains(channel))
		{
			int index = channelLists.get(player.getName()).indexOf(channel) + 1;
			channelLists.get(player.getName()).remove(channel);
			player.sendColouredMessage("Left channel %d. %s", index, channel.getName());
		}
	}

	@Override
	public IChatChannel getChannelByIndex(ICommandExecutor player, int index)
	{
		if (index < 1 || !channelLists.containsKey(player.getName()) || channelLists.get(player.getName()).size() < index)
			return null;

		return channelLists.get(player.getName()).get(index - 1);
	}

	@Override
	public void setDefaultChannel(ICommandExecutor player, IChatChannel channel)
	{
		defaultChannel.put(player.getName(), channel);
		if (channel instanceof PrivateChannel)
			player.sendMessage("Now talking in private channel.");
		else
			player.sendMessage("Now talking in channel " + channel.getName());
	}

	@Override
	public IChatChannel getDefaultChannel(ICommandExecutor player)
	{
		if (!defaultChannel.containsKey(player.getName()))
			return getChannelByName(GlobalChatChannel.CHANNELNAME);
		return defaultChannel.get(player.getName());
	}

	@Override
	public void registerChannel(IChatChannel channel)
	{
		if (channels.containsKey(channel.getName()))
			throw new RuntimeException("Unable to register duplicate channel '" + channel.getName() + "'");
		channels.put(channel.getName(), channel);
	}

	@Override
	public void unregisterChannel(IChatChannel channel)
	{
		if (channels.containsKey(channel.getName()))
			channels.remove(channel.getName());
	}

	@Override
	public IChatChannel getChannelByName(String name)
	{
		return channels.containsKey(name) ? channels.get(name) : null;
	}

	@Override
	public List<IChatChannel> getChannels(String player)
	{
		if (channelLists.containsKey(player))
			return ImmutableList.copyOf(channelLists.get(player));
		return Lists.newArrayList();
	}

	@Override
	public void processResponderHooks(IChatChannel channel, ICommandExecutor player, String message)
	{
		for(IChatResponder hook : chatResponders)
			hook.processChatMessage(channel, player, message);
	}

	@Override
	public void registerResponderHook(IChatResponder hook)
	{
		chatResponders.add(hook);
	}

	private final Map<String, IChatChannel> channels = new HashMap<String, IChatChannel>(1);
	private final List<ISpamFilter> inboundFilters;
	private final List<IChatFilter> outboundFilters;
	private final List<IChatResponder> chatResponders = new ArrayList<IChatResponder>();
	private final Map<String, List<IChatChannel>> channelLists = new HashMap<String, List<IChatChannel>>(1);
	private final Map<String, IChatChannel> defaultChannel = new HashMap<String, IChatChannel>(0);
	private final Map<String, List<String>> privateChannels = new HashMap<String, List<String>>(0);
	private final IgnoreHandler ignoreHandler;
	private final IConsole console;
	private final ChatFormatter chatFormatter;
}
