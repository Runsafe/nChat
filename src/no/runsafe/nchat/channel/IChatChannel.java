package no.runsafe.nchat.channel;

import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerChatEvent;

public interface IChatChannel
{
	boolean Join(ICommandExecutor player);
	boolean Leave(ICommandExecutor player);
	void Send(RunsafePlayerChatEvent event);
	void SendSystem(String message);
	String getName();
}
