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
	public PlayerDeath(ChatHandler chatHandler, DeathParser deathParser, IOutput console)
	{
		this.chatHandler = chatHandler;
		this.deathParser = deathParser;
		this.console = console;
	}

	@Override
	public void OnPlayerDeathEvent(RunsafePlayerDeathEvent runsafePlayerDeathEvent)
	{
		String originalMessage = runsafePlayerDeathEvent.getDeathMessage();
		Death deathType = this.deathParser.getDeathType(originalMessage);
		String deathName = deathType.name().toLowerCase();

		String deathTag;
		String entityName = null;

		if (deathType.hasEntityInvolved())
		{
			entityName = this.deathParser.getInvolvedEntityName(originalMessage);
			deathTag = String.format(
					"%s_%s",
					deathName,
					(this.deathParser.isEntityName(entityName) ? entityName : "Player")
			);
		}
		else
		{
			deathTag = deathName;
		}

		String customDeathMessage = this.deathParser.getCustomDeathMessage(deathTag);

		if (customDeathMessage != null)
		{
			RunsafePlayer player = (RunsafePlayer) runsafePlayerDeathEvent.getEntity();

			if (entityName != null && !this.deathParser.isEntityName(entityName)) // true
			{
				RunsafePlayer killer = RunsafeServer.Instance.getPlayer(entityName);

				if (killer != null)
				{
					entityName = this.chatHandler.formatPlayerName(killer);
					customDeathMessage = customDeathMessage.replace(Constants.FORMAT_PLAYER_NAME, entityName);
				}
			}

			runsafePlayerDeathEvent.setDeathMessage(this.chatHandler.formatPlayerSystemMessage(customDeathMessage, player));
		}
	}

	private ChatHandler chatHandler;
	private DeathParser deathParser;
	private IOutput console;
}
