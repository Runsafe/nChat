package no.runsafe.nchat.channel;

import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerChatEvent;
import no.runsafe.nchat.chat.EmoteEvent;

import java.util.HashMap;
import java.util.Map;

public class BasicChatChannel implements IChatChannel
{
	public BasicChatChannel(IConsole console, ChannelManager manager, String name)
	{
		this.console = console;
		this.manager = manager;
		this.name = name;
	}

	@Override
	public boolean Join(ICommandExecutor player)
	{
		if (members.containsKey(player.getName()))
			return true;

		members.put(player.getName(), player);
		return true;
	}

	@Override
	public boolean Leave(ICommandExecutor player)
	{
		if (!members.containsKey(player.getName()))
			return false;

		members.remove(player.getName());
		return true;
	}

	@Override
	public void Send(RunsafePlayerChatEvent message)
	{
		if (!message.isFake() && !members.containsKey(message.getPlayer().getName()))
			return;

		String incoming = manager.filter(message.getPlayer(), message.getMessage());
		if (incoming != null && !incoming.isEmpty())
		{
			if (message instanceof EmoteEvent)
				SendSystem(((EmoteEvent) message).getEmote());
			SendFiltered(message.getPlayer(), message.getMessage());
		}
	}

	@Override
	public void SendSystem(String message)
	{
		console.logInformation(message);
		for (ICommandExecutor member : members.values())
			member.sendColouredMessage(message);
	}

	@Override
	public String getName()
	{
		return name;
	}

	protected void SendFiltered(ICommandExecutor player, String message)
	{
		String filtered = manager.FormatMessage(player, this, message);
		if (filtered == null)
			return;
		console.logInformation(filtered);
		for (ICommandExecutor member : members.values())
			SendMessage(player, member, filtered);
	}

	protected void SendMessage(ICommandExecutor source, ICommandExecutor target, String message)
	{
		String outgoing = manager.filter(source, target, message);
		if (outgoing != null && !outgoing.isEmpty())
			target.sendColouredMessage(outgoing);
	}

	protected final IConsole console;
	protected final ChannelManager manager;
	protected final Map<String, ICommandExecutor> members = new HashMap<String, ICommandExecutor>();
	private final String name;
}