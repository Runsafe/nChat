package no.runsafe.nchat.channel;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.filter.IChatFilter;
import no.runsafe.nchat.filter.ISpamFilter;

public interface IChannelManager
{
	void registerSpamFilter(ISpamFilter filter);

	void registerChatFilter(IChatFilter filter);

	IChatChannel getPrivateChannel(IPlayer player1, IPlayer player2);

	String filter(IPlayer player, String incoming);

	String filter(IPlayer player, IPlayer member, String incoming);

	String FormatPrivateMessageLog(IPlayer you, IPlayer player, String message);

	String FormatPrivateMessageTo(IPlayer you, IPlayer player, String message);

	String FormatPrivateMessageFrom(IPlayer you, IPlayer player, String message);

	String FormatMessage(IPlayer player, IChatChannel channel, String message);

	void addChannelToList(IPlayer player, IChatChannel channel);

	void removeChannelFromList(IPlayer player, IChatChannel channel);

	IChatChannel getChannelByIndex(IPlayer player, int index);

	void setDefaultChannel(IPlayer player, IChatChannel channel);

	IChatChannel getDefaultChannel(IPlayer player);

	void registerChannel(IChatChannel channel);

	IChatChannel getChannelByName(String name);
}
