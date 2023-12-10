package no.runsafe.nchat.channel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.hook.IGlobalPluginAPI;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.internal.wrapper.player.BukkitPlayer;
import no.runsafe.nchat.Config;
import no.runsafe.nchat.chat.IgnoreHandler;
import no.runsafe.nchat.chat.formatting.ChatFormatter;
import no.runsafe.nchat.filter.IChatFilter;
import no.runsafe.nchat.filter.ISpamFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Instant;

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
		if (player1.equals(player2))
			return null;

		if (player1 instanceof IPlayer && player2 instanceof IPlayer)
			if (ignoreHandler.eitherPlayerIsIgnoring((IPlayer) player1, (IPlayer) player2))
				return null;

		String name = "%" + (player1.hashCode() + player2.hashCode());
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
			privateChannels.put(player.getName(), new ArrayList<>(1));
		privateChannels.get(player.getName()).add(channel);
	}

	@Override
	public void closePrivateChannels(ICommandExecutor player)
	{
		if (!privateChannels.containsKey(player.getName()))
			return;

		for (String channel : privateChannels.get(player.getName()))
			channels.remove(channel);

		privateChannels.remove(player.getName());
	}

	@Override
	public String filter(ICommandExecutor player, String incoming)
	{
		if (!(player instanceof IPlayer) || ((BukkitPlayer) player).getRaw() == null)
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
	public String FormatSystem(IChatChannel channel, String message)
	{
		return chatFormatter.formatSystem(channel, message);
	}

	@Override
	public void addChannelToList(ICommandExecutor player, IChatChannel channel)
	{
		if (!channelLists.containsKey(player.getName()))
			channelLists.put(player.getName(), new ArrayList<>(1));
		channelLists.get(player.getName()).add(channel);
		int index = channelLists.get(player.getName()).indexOf(channel) + 1;
		player.sendColouredMessage(Config.channelJoinMessage, index, channel.getName());
	}

	@Override
	public void removeChannelFromList(ICommandExecutor player, IChatChannel channel)
	{
		if (!channelLists.containsKey(player.getName()) || !channelLists.get(player.getName()).contains(channel))
			return;

		int index = channelLists.get(player.getName()).indexOf(channel) + 1;
		channelLists.get(player.getName()).remove(channel);
		player.sendColouredMessage(Config.channelLeaveMessage, index, channel.getName());
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
		if (Config.channelTimeoutSeconds > 0 && !channel.getName().equals(GlobalChatChannel.CHANNEL_NAME))
			channelTimeouts.put(player.getName(), Instant.now().plusSeconds(Config.channelTimeoutSeconds));
		if (channel instanceof PrivateChannel)
			player.sendColouredMessage(Config.channelPrivateMessage);
		else
			player.sendColouredMessage(Config.channelSwitchMessage, channel.getName());
	}

	@Override
	public IChatChannel getDefaultChannel(ICommandExecutor player)
	{
		if (Config.channelTimeoutSeconds > 0 && channelTimeouts.containsKey(player.getName()))
		{
			if (channelTimeouts.get(player.getName()).isAfter(Instant.now()))
				channelTimeouts.put(player.getName(), Instant.now().plusSeconds(Config.channelTimeoutSeconds));
			else
			{
				channelTimeouts.remove(player.getName());
				defaultChannel.remove(player.getName());
				player.sendColouredMessage(Config.channelSwitchMessage, GlobalChatChannel.CHANNEL_NAME);
			}
		}
		if (!defaultChannel.containsKey(player.getName()))
			return getChannelByName(GlobalChatChannel.CHANNEL_NAME);
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
		channels.remove(channel.getName());
	}

	@Override
	public IChatChannel getChannelByName(String name)
	{
		return channels.getOrDefault(name, null);
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

	@Override
	public void registerLocationTagManip(ILocationTagManipulator manipulator)
	{
		chatFormatter.registerLocationTagManip(manipulator);
	}

	private final Map<String, IChatChannel> channels = new HashMap<>(1);
	private final List<ISpamFilter> inboundFilters;
	private final List<IChatFilter> outboundFilters;
	private final List<IChatResponder> chatResponders = new ArrayList<>();
	private final Map<String, List<IChatChannel>> channelLists = new HashMap<>(1);
	private final Map<String, IChatChannel> defaultChannel = new HashMap<>(0);
	private final Map<String, List<String>> privateChannels = new HashMap<>(0);
	private final IgnoreHandler ignoreHandler;
	private final IConsole console;
	private final ChatFormatter chatFormatter;
	private final Map<String, Instant> channelTimeouts = new HashMap<>(0);
}
