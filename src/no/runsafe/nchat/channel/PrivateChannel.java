package no.runsafe.nchat.channel;

import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.RunsafeConsole;
import no.runsafe.framework.minecraft.player.RunsafeFakePlayer;

import java.time.Instant;

public class PrivateChannel extends BasicChatChannel
{
	public PrivateChannel(IChannelManager manager, IConsole console, String name, ICommandExecutor player1, ICommandExecutor player2)
	{
		super(console, manager, name);
		members.put(player1.getName(), player1);
		members.put(player2.getName(), player2);
	}

	@Override
	public boolean Join(ICommandExecutor player)
	{
		return false;
	}

	@Override
	public boolean Leave(ICommandExecutor player)
	{
		return false;
	}

	@Override
	protected void SendFiltered(ICommandExecutor sender, String message)
	{
		if (unblockedOnHiddenUntil != null && unblockedOnHiddenUntil.isBefore(Instant.now()))
			unblockedOnHiddenUntil = null;

		ICommandExecutor from = null;
		ICommandExecutor to = null;
		for (ICommandExecutor member : members.values())
			if (member.getName().equals(sender.getName()))
				from = member;
			else
				to = member;

		if (to instanceof IPlayer)
		{
			IPlayer toPlayer = (IPlayer) to;
			boolean appearOffline = !toPlayer.isOnline();
			if (from instanceof IPlayer && !(from instanceof RunsafeFakePlayer || to instanceof RunsafeFakePlayer))
			{
				IPlayer fromPlayer = (IPlayer) from;
				boolean isToHidden = fromPlayer.shouldNotSee(toPlayer);
				appearOffline |= isToHidden && unblockedOnHiddenUntil == null;

				if (!appearOffline && toPlayer.shouldNotSee(fromPlayer))
					unblockedOnHiddenUntil = Instant.now().plusSeconds(120);
			}
			if (appearOffline && from != null)
			{
				from.sendColouredMessage("&cThe player %s is currently offline.", toPlayer.getPrettyName());
				return;
			}
		}
		SendMessage(from, to, manager.FormatPrivateMessageFrom(from, to, message));
		SendMessage(to, from, manager.FormatPrivateMessageTo(from, to, message));
		if (!(from instanceof RunsafeConsole || to instanceof RunsafeConsole))
			console.logInformation(manager.FormatPrivateMessageLog(from, to, message).replace("%", "%%")+" ");
	}

	private Instant unblockedOnHiddenUntil;
}
