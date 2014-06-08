package no.runsafe.nchat.channel;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.filter.IChatFilter;
import no.runsafe.nchat.filter.ISpamFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelManager implements IConfigurationChanged
{
	public ChannelManager(List<ISpamFilter> inboundFilters, List<IChatFilter> outboundFilters)
	{
		this.outboundFilters = new ArrayList<IChatFilter>(outboundFilters);
		this.inboundFilters = new ArrayList<ISpamFilter>(inboundFilters);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		channelFormat = configuration.getConfigValueAsString("chatMessage.channel");
		messageInFormat = configuration.getConfigValueAsString("chatMessage.whisperFrom");
		messageOutFormat = configuration.getConfigValueAsString("chatMessage.whisperTo");
	}

	public void registerSpamFilter(ISpamFilter filter)
	{
		inboundFilters.add(filter);
	}

	public void registerChatFilter(IChatFilter filter)
	{
		outboundFilters.add(filter);
	}

	public IChatChannel getPrivateChannel(IPlayer player1, IPlayer player2)
	{
		int cmp = player1.getName().compareToIgnoreCase(player2.getName());
		if (cmp == 0)
			return null;

		String name =
			"%" + (cmp < 0 ? player1.getName() : player2.getName()) +
			"-" + (cmp > 0 ? player1.getName() : player2.getName());
		if (!channels.containsKey(name))
			channels.put(name, new PrivateChannel(this, name, player1, player2));

		return channels.get(name);
	}

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

	public String FormatPrivateMessageTo(IPlayer you, IPlayer player, String message)
	{
		return messageOutFormat
			.replace("#source", you.getPrettyName())
			.replace("#target", player.getPrettyName())
			.replace("#message", message);
	}

	public String FormatPrivateMessageFrom(IPlayer you, IPlayer player, String message)
	{
		return messageInFormat
			.replace("#source", you.getPrettyName())
			.replace("#target", player.getPrettyName())
			.replace("#message", message);
	}

	public String FormatMessage(IPlayer player, IChatChannel channel, String message)
	{
		return messageInFormat
			.replace("#tag", channel.getName())
			.replace("#player", player.getPrettyName())
			.replace("#message", message);
	}

	public void addChannelToList(IPlayer player, IChatChannel channel)
	{
		if(!channelLists.containsKey(player.getName()))
			channelLists.put(player.getName(), new ArrayList<IChatChannel>(1));
		channelLists.get(player.getName()).add(channel);
	}

	public void removeChannelFromList(IPlayer player, IChatChannel channel)
	{
		if(channelLists.containsKey(player.getName()) && channelLists.get(player.getName()).contains(channel))
			channelLists.get(player.getName()).remove(channel);
	}

	public IChatChannel getChannelByIndex(IPlayer player, int index)
	{
		if(!channelLists.containsKey(player.getName()) || channelLists.get(player.getName()).size() <= index)
			return null;

		return channelLists.get(player.getName()).get(index);
	}

	public void setDefaultChannel(IPlayer player, IChatChannel channel)
	{
		defaultChannel.put(player.getName(), channel);
	}

	public IChatChannel getDefaultChannel(IPlayer player)
	{
		if (!defaultChannel.containsKey(player.getName()))
			return null;
		return defaultChannel.get(player.getName());
	}

	private final Map<String, IChatChannel> channels = new HashMap<String, IChatChannel>(1);
	private final List<ISpamFilter> inboundFilters;
	private final List<IChatFilter> outboundFilters;
	private final Map<String, List<IChatChannel>> channelLists = new HashMap<String, List<IChatChannel>>();
	private final Map<String, IChatChannel> defaultChannel = new HashMap<String, IChatChannel>();
	private String channelFormat;
	private String messageOutFormat;
	private String messageInFormat;
}
