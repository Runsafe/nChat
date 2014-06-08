package no.runsafe.nchat.channel;

import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;

public class PrivateChannel extends BasicChatChannel
{
	public PrivateChannel(ChannelManager manager, IConsole console, String name, IPlayer player1, IPlayer player2)
	{
		super(console, manager, name);
		members.put(player1.getName(), player1);
		members.put(player2.getName(), player2);
	}

	@Override
	public boolean Join(IPlayer player)
	{
		return false;
	}

	@Override
	public boolean Leave(IPlayer player)
	{
		return false;
	}

	@Override
	protected void SendFiltered(IPlayer from, String message)
	{
		IPlayer to = null;
		for(IPlayer member : members.values())
			if(!member.getName().equals(from.getName()))
				to = member;
		SendMessage(from, to, manager.FormatPrivateMessageFrom(from, to, message));
		SendMessage(from, to, manager.FormatPrivateMessageTo(from, to, message));
		console.logInformation(manager.FormatPrivateMessageLog(from, to, message));
	}
}
