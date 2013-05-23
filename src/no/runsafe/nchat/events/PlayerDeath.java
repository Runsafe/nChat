package no.runsafe.nchat.events;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.player.IPlayerDeathEvent;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.entity.RunsafeEntity;
import no.runsafe.framework.server.event.player.RunsafePlayerDeathEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Death;
import no.runsafe.nchat.DeathParser;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeath implements IPlayerDeathEvent, IConfigurationChanged
{
	public PlayerDeath(DeathParser deathParser, IOutput console)
	{
		this.deathParser = deathParser;
		this.console = console;
	}

	@Override
	public void OnPlayerDeathEvent(RunsafePlayerDeathEvent event)
	{
		String originalMessage = event.getDeathMessage();
		event.setDeathMessage("");

		RunsafeEntity entity = event.getEntity();
		if (!this.hideDeathWorlds.contains(entity.getWorld().getName()))
		{
			RunsafeServer.Instance.broadcastMessage("Cause of dmg: " + event.getEntity().getLastDamageCause().getCause().name());
			RunsafeServer.Instance.broadcastMessage("Killer: " + event.getEntity().getLastDamageCause().getEntity().getEntityType().getName());
		}
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.hideDeathWorlds = iConfiguration.getConfigValueAsList("hideDeaths");
	}

	private final DeathParser deathParser;
	private final IOutput console;
	private List<String> hideDeathWorlds = new ArrayList<String>();
}
