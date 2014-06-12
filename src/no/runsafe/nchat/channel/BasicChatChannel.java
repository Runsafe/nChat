package no.runsafe.nchat.channel;

import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerChatEvent;
import no.runsafe.nchat.chat.EmoteEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class BasicChatChannel implements IChatChannel
{
	public BasicChatChannel(IConsole console, IChannelManager manager, String name)
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

		manager.addChannelToList(player, this);
		members.put(player.getName(), player);
		return true;
	}

	@Override
	public boolean Leave(ICommandExecutor player)
	{
		if (!members.containsKey(player.getName()))
			return false;

		manager.removeChannelFromList(player, this);
		members.remove(player.getName());
		return true;
	}

	@Override
	public void Send(RunsafePlayerChatEvent message)
	{
		if (!message.isFake() && !members.containsKey(message.getPlayer().getName()))
			return;

		String incoming = message.isFake() ? message.getMessage() : manager.filter(message.getPlayer(), message.getMessage());
		if (incoming != null && !incoming.isEmpty())
		{
			if (message instanceof EmoteEvent)
				SendSystem(((EmoteEvent) message).getEmote());
			else
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
	@Nonnull
	public String getName()
	{
		return name;
	}

	@Nullable
	@Override
	public String getCustomTag()
	{
		return null;
	}

	protected void SendFiltered(ICommandExecutor player, String message)
	{
		String filtered = manager.FormatMessage(player, this, message);
		if (filtered == null)
			return;
		manager.processResponderHooks(this, player, message);
		console.logInformation(filtered.replace("%","%%"));
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
	protected final IChannelManager manager;
	protected final Map<String, ICommandExecutor> members = new HashMap<String, ICommandExecutor>();
	private final String name;
}
