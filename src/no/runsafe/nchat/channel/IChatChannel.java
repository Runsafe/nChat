package no.runsafe.nchat.channel;

import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerChatEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IChatChannel
{
	boolean Join(ICommandExecutor player);

	boolean Leave(ICommandExecutor player);

	void Clear();

	void Send(RunsafePlayerChatEvent event);

	void SendSystem(String message);

	@Nonnull
	String getName();

	@Nullable
	String getCustomTag();
}
