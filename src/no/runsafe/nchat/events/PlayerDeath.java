package no.runsafe.nchat.events;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.event.player.IPlayerDeathEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.event.entity.RunsafeEntityDamageEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerDeathEvent;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDeath implements IPlayerDeathEvent, IConfigurationChanged
{
	public PlayerDeath(IOutput output)
	{
		this.output = output;
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
				this.output.broadcastColoured(String.format(this.deathMessages.get(deathType), player.getPrettyName()));
			else
				this.output.warning(String.format("%s experienced an unregistered death: %s", player.getName(), deathType));
		}
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.hideDeathWorlds = iConfiguration.getConfigValueAsList("hideDeaths");
		this.deathMessages = iConfiguration.getConfigValuesAsMap("deathMessages");
	}

	private final IOutput output;
	private List<String> hideDeathWorlds = new ArrayList<String>();
	private Map<String, String> deathMessages = new HashMap<String, String>();
}
