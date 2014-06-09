package no.runsafe.nchat.channel;

import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.nchat.filter.IChatFilter;
import no.runsafe.nchat.filter.ISpamFilter;

public interface IChannelManager
{
	void registerSpamFilter(ISpamFilter filter);

	void registerChatFilter(IChatFilter filter);

	IChatChannel getPrivateChannel(ICommandExecutor player1, ICommandExecutor player2);

	String filter(ICommandExecutor player, String incoming);

	String filter(ICommandExecutor player, ICommandExecutor member, String incoming);

	String FormatPrivateMessageLog(ICommandExecutor you, ICommandExecutor player, String message);

	String FormatPrivateMessageTo(ICommandExecutor you, ICommandExecutor player, String message);

	String FormatPrivateMessageFrom(ICommandExecutor you, ICommandExecutor player, String message);

	String FormatMessage(ICommandExecutor player, IChatChannel channel, String message);

	void addChannelToList(ICommandExecutor player, IChatChannel channel);

	void removeChannelFromList(ICommandExecutor player, IChatChannel channel);

	IChatChannel getChannelByIndex(ICommandExecutor player, int index);

	void setDefaultChannel(ICommandExecutor player, IChatChannel channel);

	IChatChannel getDefaultChannel(ICommandExecutor player);

	void registerChannel(IChatChannel channel);

	IChatChannel getChannelByName(String name);
}
