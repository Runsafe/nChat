package no.runsafe.nchat.deathhandler;

import no.runsafe.framework.minecraft.entity.RunsafeEntity;
import no.runsafe.framework.minecraft.event.entity.RunsafeEntityDamageEvent;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

public class PlayerDeathMeta
{
	public PlayerDeathMeta(RunsafePlayer player, RunsafeEntityDamageEvent.RunsafeDamageCause type)
	{
		this.player = player;
		this.type = type;
	}

	public RunsafeEntityDamageEvent.RunsafeDamageCause getType()
	{
		return this.type;
	}

	public RunsafePlayer getPlayer()
	{
		return this.player;
	}

	public void setEntity(RunsafeEntity entity)
	{
		this.entity = entity;
	}

	public RunsafePlayer getEnemyPlayer()
	{
		return (this.hasEnemyPlayer() ? (RunsafePlayer) this.entity : null);
	}

	public RunsafeEntity getEntity()
	{
		return this.entity;
	}

	public boolean hasEnemyPlayer()
	{
		return this.entity instanceof RunsafePlayer;
	}

	private final RunsafeEntityDamageEvent.RunsafeDamageCause type;
	private final RunsafePlayer player;
	private RunsafeEntity entity;
}
