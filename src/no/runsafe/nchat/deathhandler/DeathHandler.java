package no.runsafe.nchat.deathhandler;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerDeathEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.RunsafeWorld;
import no.runsafe.framework.minecraft.event.entity.RunsafeEntityDamageByEntityEvent;
import no.runsafe.framework.minecraft.event.entity.RunsafeEntityDamageEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerDeathEvent;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.List;

public class DeathHandler implements IPlayerDeathEvent, IConfigurationChanged
{
	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		this.hideDeathWorlds = config.getConfigValueAsList("hideDeaths");
	}

	@Override
	public void OnPlayerDeathEvent(RunsafePlayerDeathEvent event)
	{
		RunsafePlayer player = event.getEntity();
		if (!this.hideDeathsForWorld(player.getWorld()))
		{
			RunsafeEntityDamageEvent damageEvent = player.getLastDamageCause();
			PlayerDeathMeta meta = new PlayerDeathMeta(player, damageEvent.getCause());

			if (damageEvent instanceof RunsafeEntityDamageByEntityEvent)
			{
				RunsafeEntityDamageByEntityEvent damageByEntityEvent = (RunsafeEntityDamageByEntityEvent) damageEvent;
				meta.setEntity(damageByEntityEvent.getDamageActor());
			}
			this.handleDeath(meta);
		}
	}

	private boolean hideDeathsForWorld(RunsafeWorld world)
	{
		return this.hideDeathWorlds.contains(world.getName());
	}

	private void handleDeath(PlayerDeathMeta meta)
	{

	}

	private List<String> hideDeathWorlds = new ArrayList<String>();
}
