package no.runsafe.nchat.channel;

import no.runsafe.framework.api.player.IPlayer;

public class PrivateChannel extends BasicChatChannel
{
	public PrivateChannel(ChannelManager manager, String name, IPlayer player1, IPlayer player2)
	{
		super(manager, name);
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
	protected void SendFiltered(IPlayer player, String message)
	{
		for(IPlayer member : members.values())
			SendMessage(
				player,
				member,
				player.getName().equals(member.getName())
					? manager.FormatPrivateMessageFrom(player, member, message)
					: manager.FormatPrivateMessageTo(player, member, message)
			);
	}
}
