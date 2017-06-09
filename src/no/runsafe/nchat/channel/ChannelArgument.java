package no.runsafe.nchat.channel;

import no.runsafe.framework.api.command.argument.CommandArgumentSpecification;
import no.runsafe.framework.api.command.argument.IContextualTabComplete;
import no.runsafe.framework.api.player.IPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChannelArgument extends CommandArgumentSpecification<IChatChannel> implements IContextualTabComplete
{
	public ChannelArgument(IChannelManager manager)
	{
		super("channel");
		this.manager = manager;
	}

	@Override
	public boolean isRequired()
	{
		return true;
	}

	@Override
	public boolean isWhitespaceInclusive()
	{
		return false;
	}

	@Override
	public IChatChannel getValue(IPlayer context, Map<String, String> params)
	{
		return manager.getChannelByName(params.get(name));
	}

	@Override
	public List<String> getAlternatives(IPlayer executor, String partial, String... predecessors)
	{
		List<IChatChannel> channels = manager.getChannels(predecessors[0]);
		if (channels == null)
			return null;
		List<String> values = new ArrayList<>(channels.size());
		for(IChatChannel channel : channels)
			values.add(channel.getName());
		return values;
	}

	private final IChannelManager manager;
}
