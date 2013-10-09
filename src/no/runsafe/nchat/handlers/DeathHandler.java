package no.runsafe.nchat.handlers;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerDeathEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerDeathEvent;

public class DeathHandler implements IPlayerDeathEvent, IConfigurationChanged
{
	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		deathMessage = config.getConfigValueAsString("chatMessage.death");
	}

	@Override
	public void OnPlayerDeathEvent(RunsafePlayerDeathEvent event)
	{
		if (deathMessage != null) // If we're null it's missing on config, just ignore.
			event.setDeathMessage(deathMessage.replaceAll("#player", event.getEntity().getPrettyName()));
	}

	private String deathMessage;
}
