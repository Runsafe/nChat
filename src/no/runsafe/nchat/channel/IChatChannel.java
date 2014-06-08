package no.runsafe.nchat.channel;

import no.runsafe.framework.api.player.IPlayer;

public interface IChatChannel
{
	boolean Join(IPlayer player);
	boolean Leave(IPlayer player);
	void Send(IPlayer player, String message);
	String getName();
}
