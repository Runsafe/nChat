package no.runsafe.nchat.channel;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.IgnoreHandler;
import no.runsafe.nchat.filter.IChatFilter;
import no.runsafe.nchat.filter.ISpamFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelManager implements IConfigurationChanged, IChannelManager
{
	public ChannelManager(List<ISpamFilter> inboundFilters, List<IChatFilter> outboundFilters, IConsole console, IgnoreHandler ignoreHandler)
	{
		this.console = console;
		this.ignoreHandler = ignoreHandler;
		this.outboundFilters = new ArrayList<IChatFilter>(outboundFilters);
		this.inboundFilters = new ArrayList<ISpamFilter>(inboundFilters);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		channelFormat = configuration.getConfigValueAsString("chatMessage.channel");
		messageInFormat = configuration.getConfigValueAsString("chatMessage.whisperFrom");
		messageOutFormat = configuration.getConfigValueAsString("chatMessage.whisperTo");
		messageLogFormat = configuration.getConfigValueAsString("chatMessage.whisperLog");
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
	public IChatChannel getPrivateChannel(IPlayer player1, IPlayer player2)
	{
		int cmp = player1.getName().compareToIgnoreCase(player2.getName());
		if (cmp == 0)
			return null;

		if(ignoreHandler.playerIsIgnoring(player1, player2))
		{
			player1.sendMessage("You are being ignored.");
			return null;
		}

		if(ignoreHandler.playerIsIgnoring(player2, player1))
		{
			player1.sendMessage("You are ignoring that player.");
			return null;
		}

		String name =
			"%" + (cmp < 0 ? player1.getName() : player2.getName()) +
				"-" + (cmp > 0 ? player1.getName() : player2.getName());
		if (!channels.containsKey(name))
			channels.put(name, new PrivateChannel(this, console, name, player1, player2));

		return channels.get(name);
	}

	@Override
	public String filter(IPlayer player, String incoming)
	{
		String message = incoming;
		for (ISpamFilter filter : inboundFilters)
		{
			if (message == null || message.isEmpty())
				return null;
			message = filter.processString(player, message);
		}
		return message;
	}

	@Override
	public String filter(IPlayer player, IPlayer member, String incoming)
	{
		String message = incoming;
		for (IChatFilter filter : outboundFilters)
		{
			if (message == null || message.isEmpty())
				return null;
			message = filter.processString(player, member, message);
		}
		return message;
	}

	@Override
	public String FormatPrivateMessageLog(IPlayer you, IPlayer player, String message)
	{
		return messageLogFormat
			.replace("#source", you.getPrettyName())
			.replace("#target", player.getPrettyName())
			.replace("#message", message);
	}

	@Override
	public String FormatPrivateMessageTo(IPlayer you, IPlayer player, String message)
	{
		return messageOutFormat
			.replace("#source", you.getPrettyName())
			.replace("#target", player.getPrettyName())
			.replace("#message", message);
	}

	@Override
	public String FormatPrivateMessageFrom(IPlayer you, IPlayer player, String message)
	{
		return messageInFormat
			.replace("#source", you.getPrettyName())
			.replace("#target", player.getPrettyName())
			.replace("#message", message);
	}

	@Override
	public String FormatMessage(IPlayer player, IChatChannel channel, String message)
	{
		return channelFormat
			.replace("#tag", channel.getName())
			.replace("#player", player.getPrettyName())
			.replace("#message", message);
	}

	@Override
	public void addChannelToList(IPlayer player, IChatChannel channel)
	{
		if (!channelLists.containsKey(player.getName()))
			channelLists.put(player.getName(), new ArrayList<IChatChannel>(1));
		channelLists.get(player.getName()).add(channel);
	}

	@Override
	public void removeChannelFromList(IPlayer player, IChatChannel channel)
	{
		if (channelLists.containsKey(player.getName()) && channelLists.get(player.getName()).contains(channel))
			channelLists.get(player.getName()).remove(channel);
	}

	@Override
	public IChatChannel getChannelByIndex(IPlayer player, int index)
	{
		if (!channelLists.containsKey(player.getName()) || channelLists.get(player.getName()).size() <= index)
			return null;

		return channelLists.get(player.getName()).get(index);
	}

	@Override
	public void setDefaultChannel(IPlayer player, IChatChannel channel)
	{
		defaultChannel.put(player.getName(), channel);
	}

	@Override
	public IChatChannel getDefaultChannel(IPlayer player)
	{
		if (!defaultChannel.containsKey(player.getName()))
			return null;
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
	public IChatChannel getChannelByName(String name)
	{
		return channels.containsKey(name) ? channels.get(name) : null;
	}

	private final Map<String, IChatChannel> channels = new HashMap<String, IChatChannel>(1);
	private final List<ISpamFilter> inboundFilters;
	private final List<IChatFilter> outboundFilters;
	private final Map<String, List<IChatChannel>> channelLists = new HashMap<String, List<IChatChannel>>();
	private final Map<String, IChatChannel> defaultChannel = new HashMap<String, IChatChannel>();
	private final IConsole console;
	private final IgnoreHandler ignoreHandler;
	private String channelFormat;
	private String messageOutFormat;
	private String messageInFormat;
	private String messageLogFormat;
}
