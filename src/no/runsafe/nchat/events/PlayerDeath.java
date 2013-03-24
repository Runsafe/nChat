package no.runsafe.nchat.events;

import no.runsafe.framework.event.player.IPlayerDeathEvent;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.event.player.RunsafePlayerDeathEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.Death;
import no.runsafe.nchat.DeathParser;
import no.runsafe.nchat.handlers.ChatHandler;

public class PlayerDeath implements IPlayerDeathEvent
{
	public PlayerDeath(ChatHandler chatHandler, DeathParser deathParser)
	{
		this.chatHandler = chatHandler;
		this.deathParser = deathParser;
	}

	@Override
	public void OnPlayerDeathEvent(RunsafePlayerDeathEvent runsafePlayerDeathEvent)
	{
		String originalMessage = runsafePlayerDeathEvent.getDeathMessage();
		Death deathType = this.deathParser.getDeathType(originalMessage);
		String deathName = deathType.name().toLowerCase();

		String deathTag;
		String entityName = null;
		String killerName = this.deathParser.getInvolvedEntityName(originalMessage, deathType);


		if (deathType.hasEntityInvolved())
		{
			entityName = this.deathParser.isEntityName(killerName);
			deathTag = String.format(
					"%s_%s",
					deathName,
					(entityName != null ? entityName : "Player")
			);
		}
		else
		{
			deathTag = deathName;
		}

		String customDeathMessage = this.deathParser.getCustomDeathMessage(deathTag);

		if (customDeathMessage != null)
		{
			RunsafePlayer player = runsafePlayerDeathEvent.getEntity();

			if (entityName == null) // true
			{
				RunsafePlayer killer = RunsafeServer.Instance.getPlayer(killerName);

				if (killer != null)
				{
					killerName = this.chatHandler.formatPlayerName(killer, killer.getName());
					customDeathMessage = customDeathMessage.replace(Constants.FORMAT_PLAYER_NAME, killerName);
				}
			}

			runsafePlayerDeathEvent.setDeathMessage(this.chatHandler.formatPlayerSystemMessage(customDeathMessage, player));
		}
	}

	private final ChatHandler chatHandler;
	private final DeathParser deathParser;
}
