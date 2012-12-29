package no.runsafe.nchat;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public class DeathParser implements IConfigurationChanged
{
	public Death getDeathType(String deathMessage)
	{
		for (Death death : Death.values())
			if (deathMessage.contains(death.getDefaultMessage()))
				return death;

		return Death.UNKNOWN;
	}

	public String getInvolvedEntityName(String deathMessage)
	{
		String[] split = deathMessage.split("\\s");
		return split[split.length-1];
	}

	public boolean isEntityName(String entityName)
	{
		return (EntityType.fromName(entityName) != null);
	}

	public String getCustomDeathMessage(String deathTag)
	{
		return (this.deathMessages.contains(deathTag)) ? (String) this.deathMessages.get(deathTag) : null;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.deathMessages = iConfiguration.getSection("deathMessages");
	}

	private ConfigurationSection deathMessages;
}
