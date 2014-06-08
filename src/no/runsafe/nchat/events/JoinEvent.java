package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerJoinEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerJoinEvent;
import no.runsafe.nchat.tablist.PlayerTablistNameHandler;

public class JoinEvent implements IPlayerJoinEvent
{
	public JoinEvent(PlayerTablistNameHandler tabListHandler)
	{
		this.tabListHandler = tabListHandler;
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent event)
	{
		event.setJoinMessage(null);
		IPlayer player = event.getPlayer();
		tabListHandler.refreshPlayerTabListName(player);
	}

	private final PlayerTablistNameHandler tabListHandler;
}
