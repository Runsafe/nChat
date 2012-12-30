package no.runsafe.nchat;

public enum EntityDeath
{
	Enderman("Enderman"),
	Wolf("Wolf"),
	PigZombie("Zombie Pigman"),
	VillagerGolem("Iron Golem"),
	EnderDragon("Ender Dragon"),
	Wither("Wither"),
	Giant("Giant"),
	Blaze("Blaze"),
	CaveSpider("Cave Spider"),
	Creeper("Creeper"),
	Ghast("Ghast"),
	LavaSlime("Magma Cube"),
	Silverfish("Silverfish"),
	Skeleton("Skeleton"),
	Slime("Slime"),
	Spider("Spider"),
	Witch("Witch"),
	Zombie("Zombie");

	private final String deathName;

	EntityDeath(String deathName)
	{
		this.deathName = deathName;
	}

	public String getDeathName()
	{
		return this.deathName;
	}
}
