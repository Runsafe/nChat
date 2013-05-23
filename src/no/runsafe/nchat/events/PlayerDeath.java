package no.runsafe.nchat.events;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.player.IPlayerDeathEvent;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.event.entity.RunsafeEntityDamageEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerDeathEvent;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDeath implements IPlayerDeathEvent, IConfigurationChanged
{
	public PlayerDeath(IOutput console)
	{
		this.console = console;
	}

	@Override
	public void OnPlayerDeathEvent(RunsafePlayerDeathEvent event)
	{
		event.setDeathMessage("");
		RunsafePlayer player = event.getEntity();

		if (!this.hideDeathWorlds.contains(player.getWorld().getName()))
		{
			RunsafeEntityDamageEvent cause = player.getLastDamageCause();
			String deathType = cause.getCause().name().toLowerCase();

			if (this.deathMessages.containsKey(deathType))
				this.console.broadcastColoured(String.format(this.deathMessages.get(deathType), player.getPrettyName()));
			else
				this.console.warning(String.format("%s experienced an unregistered death: %s", player.getName(), deathType));
		}
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.hideDeathWorlds = iConfiguration.getConfigValueAsList("hideDeaths");
		this.deathMessages = iConfiguration.getConfigValuesAsMap("deathMessages");
	}

	private final IOutput console;
	private List<String> hideDeathWorlds = new ArrayList<String>();
	private Map<String, String> deathMessages = new HashMap<String, String>();
}
