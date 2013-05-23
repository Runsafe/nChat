package no.runsafe.nchat;

public enum Death
{
	ANVIL("%s was squashed by a falling anvil"),
	CACTUS("%s was pricked to death"),
	CACTUS_FLEE("%s walked into a cactus whilst trying to escape %s"),
	ARROW("%s was shot by arrow"),
	DROWNED("%s drowned"),
	DROWNED_FLEE("%s drowned while trying to escape %s"),
	EXPLOSION("%s blew up"),
	EXPLOSION_BY("%s was blown up by %s"),
	FALL("%s hit the ground too hard"),
	FALL_LADDER("%s fell off a ladder"),
	FALL_VINE("%s fell off some vines"),
	FALL_WATER("%s fell out of the water"),
	FALL_HIGH("%s fell from a high place"),
	FALL_FIRE("%s fell into a patch of fire"),
	FALL_CACTI("%s fell into a patch of cacti"),
	FALL_GHAST("%s was blown from a high place"),
	FALL_DOOMED("%s was doomed to fall by %s"),
	FALL_DOOM("%s was doomed to fall"),
	FALL_VINE_PUSH("%s was shot off some vines by %s"),
	FALL_LADDER_PUSH("%s was shot off a ladder by %s"),
	FIRE("%s went up in flames"),
	BURNING("%s burned to death"),
	BURNING_WHILE("%s was burnt to a crisp whilst fighting %s"),
	FIRE_WHILE("%s walked into fire whilst fighting %s"),
	SLAIN("%s was slain by %s"),
	SHOT("%s was shot by %s"),
	FIREBALL("%s was fireballed by %s"),
	KILLED("%s was killed by %s"),
	WEAPON1("%s got finished off by %s using %s"),
	WEAPON2("%s got slain by %s using %s"),
	LAVA("%s tried to swim in lava"),
	LAVA_FLEE("%s tried to swim in lava while trying to escape %s"),
	UNKNOWN("%s died"),
	MAGIC("%s was killed by magic"),
	STARVATION("%s starved to death"),
	SUFFOCATION("%s suffocated in a wall"),
	THORNS("%s was killed while trying to hurt %s"),
	PUMMELED("%s was pummeled by %s"),
	VOID("%s fell out of the world"),
	VOID_HIGH("%s fell from a high place and fell out of the world"),
	VOID_KNOCK("%s was knocked into the void by %s"),
	WITHER("%s withered away");

	private final String defaultMessage;

	Death(String defaultMessage)
	{
		this.defaultMessage = defaultMessage;
	}

	public String getDefaultMessage()
	{
		return this.defaultMessage;
	}
}
