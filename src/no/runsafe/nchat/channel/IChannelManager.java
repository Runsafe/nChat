package no.runsafe.nchat.channel;

import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.nchat.filter.IChatFilter;
import no.runsafe.nchat.filter.ISpamFilter;

import java.util.List;

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

	String FormatSystem(IChatChannel channel, String incoming);

	void addChannelToList(ICommandExecutor player, IChatChannel channel);

	void removeChannelFromList(ICommandExecutor player, IChatChannel channel);

	IChatChannel getChannelByIndex(ICommandExecutor player, int index);

	void setDefaultChannel(ICommandExecutor player, IChatChannel channel);

	IChatChannel getDefaultChannel(ICommandExecutor player);

	void registerChannel(IChatChannel channel);

	void unregisterChannel(IChatChannel channel);

	IChatChannel getChannelByName(String name);

	List<IChatChannel> getChannels(String player);

	void processResponderHooks(IChatChannel channel, ICommandExecutor player, String message);

	void registerResponderHook(IChatResponder hook);

	void closePrivateChannels(ICommandExecutor player);

	void registerLocationTagManip(ILocationTagManipulator manipulator);
}

