package no.runsafe.nchat.channel;

import no.runsafe.framework.api.player.IPlayer;

import java.util.HashMap;
import java.util.Map;

public class BasicChatChannel implements IChatChannel
{
	public BasicChatChannel(ChannelManager manager, String name)
	{
		this.manager = manager;
		this.name = name;
	}

	@Override
	public boolean Join(IPlayer player)
	{
		if (members.containsKey(player.getName()))
			return true;

		members.put(player.getName(), player);
		return true;
	}

	@Override
	public boolean Leave(IPlayer player)
	{
		if (!members.containsKey(player.getName()))
			return false;

		members.remove(player.getName());
		return true;
	}

	@Override
	public void Send(IPlayer player, String message)
	{
		if (!members.containsKey(player.getName()))
			return;

		String incoming = manager.filter(player, message);
		if (incoming != null && !incoming.isEmpty())
			SendFiltered(player, incoming);
	}

	@Override
	public String getName()
	{
		return name;
	}

	protected void SendFiltered(IPlayer player, String message)
	{
		for (IPlayer member : members.values())
			SendMessage(player, member, message);
	}

	protected void SendMessage(IPlayer source, IPlayer target, String message)
	{
		String outgoing = manager.filter(source, target, message);
		if(outgoing != null && !outgoing.isEmpty())
			target.sendColouredMessage(outgoing);
	}

	protected final ChannelManager manager;
	protected final Map<String, IPlayer> members = new HashMap<String, IPlayer>();
	private final String name;
}
