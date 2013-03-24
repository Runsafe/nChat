package no.runsafe.nchat;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.output.IOutput;
import org.bukkit.configuration.ConfigurationSection;

public class DeathParser implements IConfigurationChanged
{
	public DeathParser(IOutput console)
	{
		this.console = console;
	}

	public Death getDeathType(String deathMessage)
	{
		for (Death death : Death.values())
			if (deathMessage.contains(death.getDefaultMessage()))
				return death;

		return Death.UNKNOWN;
	}

	public String getInvolvedEntityName(String deathMessage, Death death)
	{
		return deathMessage.substring(
				deathMessage.indexOf(death.getDefaultMessage()) + death.getDefaultMessage().length(),
				deathMessage.length()
		);
	}

	public String isEntityName(String entityName)
	{
		for (EntityDeath entity : EntityDeath.values())
		{
			if (entity.getDeathName().equals(entityName))
				return entity.name();
		}
		return null;
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
	private final IOutput console;
}
