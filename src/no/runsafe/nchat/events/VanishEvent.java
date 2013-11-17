package no.runsafe.nchat.events;

import no.runsafe.framework.api.event.player.IPlayerCustomEvent;
import no.runsafe.framework.minecraft.event.player.RunsafeCustomEvent;
import no.runsafe.nchat.tablist.TabListHandler;

public class VanishEvent implements IPlayerCustomEvent
{
	public VanishEvent(TabListHandler tabListHandler)
	{
		this.tabListHandler = tabListHandler;
	}

	@Override
	public void OnPlayerCustomEvent(RunsafeCustomEvent event)
	{
		if (event.getEvent().equalsIgnoreCase("vanished"))
			tabListHandler.refreshPlayerTabListName(event.getPlayer());
	}

	private final TabListHandler tabListHandler;
}
