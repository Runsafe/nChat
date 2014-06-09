package no.runsafe.nchat.channel;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.hook.IGlobalPluginAPI;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.IgnoreHandler;
import no.runsafe.nchat.filter.IChatFilter;
import no.runsafe.nchat.filter.ISpamFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelManager implements IConfigurationChanged, IChannelManager, IGlobalPluginAPI
{
	public ChannelManager(List<ISpamFilter> inboundFilters, List<IChatFilter> outboundFilters, IgnoreHandler ignoreHandler, IConsole console)
	{
		this.inboundFilters = inboundFilters;
		this.outboundFilters = outboundFilters;
		this.ignoreHandler = ignoreHandler;
		this.console = console;
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
			channels.put(name, new PrivateChannel(this, console, name, player1, player2));

		return channels.get(name);
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
		return messageLogFormat
			.replace("#source", getName(you))
			.replace("#target", getName(player))
			.replace("#message", message);
	}

	@Override
	public String FormatPrivateMessageTo(ICommandExecutor you, ICommandExecutor player, String message)
	{
		return messageOutFormat
			.replace("#source", getName(you))
			.replace("#target", getName(player))
			.replace("#message", message);
	}

	@Override
	public String FormatPrivateMessageFrom(ICommandExecutor you, ICommandExecutor player, String message)
	{
		return messageInFormat
			.replace("#source", getName(you))
			.replace("#target", getName(player))
			.replace("#message", message);
	}

	@Override
	public String FormatMessage(ICommandExecutor player, IChatChannel channel, String message)
	{
		return channelFormat
			.replace("#tag", channel.getName())
			.replace("#player", getName(player))
			.replace("#message", message);
	}

	private String getName(ICommandExecutor executor)
	{
		return executor instanceof IPlayer ? ((IPlayer) executor).getPrettyName() : executor.getName();
	}

	@Override
	public void addChannelToList(ICommandExecutor player, IChatChannel channel)
	{
		if (!channelLists.containsKey(player.getName()))
			channelLists.put(player.getName(), new ArrayList<IChatChannel>(1));
		channelLists.get(player.getName()).add(channel);
	}

	@Override
	public void removeChannelFromList(ICommandExecutor player, IChatChannel channel)
	{
		if (channelLists.containsKey(player.getName()) && channelLists.get(player.getName()).contains(channel))
			channelLists.get(player.getName()).remove(channel);
	}

	@Override
	public IChatChannel getChannelByIndex(ICommandExecutor player, int index)
	{
		if (!channelLists.containsKey(player.getName()) || channelLists.get(player.getName()).size() <= index)
			return null;

		return channelLists.get(player.getName()).get(index);
	}

	@Override
	public void setDefaultChannel(ICommandExecutor player, IChatChannel channel)
	{
		defaultChannel.put(player.getName(), channel);
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
	public IChatChannel getChannelByName(String name)
	{
		return channels.containsKey(name) ? channels.get(name) : null;
	}

	private final Map<String, IChatChannel> channels = new HashMap<String, IChatChannel>(1);
	private final List<ISpamFilter> inboundFilters;
	private final List<IChatFilter> outboundFilters;
	private final Map<String, List<IChatChannel>> channelLists = new HashMap<String, List<IChatChannel>>();
	private final Map<String, IChatChannel> defaultChannel = new HashMap<String, IChatChannel>();
	private final IgnoreHandler ignoreHandler;
	private final IConsole console;
	private String channelFormat;
	private String messageOutFormat;
	private String messageInFormat;
	private String messageLogFormat;
}
