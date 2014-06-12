package no.runsafe.nchat.channel;

import no.runsafe.framework.api.command.ICommandExecutor;

public interface IChatResponder
{
	void processChatMessage(IChatChannel channel, ICommandExecutor player, String message);
}
