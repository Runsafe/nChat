package no.runsafe.nchat.handlers;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;
import no.runsafe.nchat.PlayerSpamInfo;

import java.util.HashMap;

public class SpamHandler implements IConfigurationChanged
{
	public SpamHandler(IScheduler scheduler)
	{
		this.spamInfo = new HashMap<RunsafePlayer, PlayerSpamInfo>();
		this.scheduler = scheduler;
	}

	public void checkForSpam(final RunsafePlayer player, String message)
	{
		if (this.spamInfo.containsKey(player))
		{
			PlayerSpamInfo spamInfo = this.spamInfo.get(player);

			if (!spamInfo.hasFloodTimerRunning())
			{
				this.scheduler.startAsyncTask(new Runnable()
				{
					@Override
					public void run()
					{
						clearPlayerSpamHistory(player);
					}
				}, this.spamFilterFloodTimer);
			}
			spamInfo.addMessageToHistory(message);

			if (spamInfo.currentFloodCount() == spamFilterFloodAmount)
				player.kick(this.spamFilterKickReason);
		}
		else
		{
			PlayerSpamInfo spamInfo = new PlayerSpamInfo();
			spamInfo.addMessageToHistory(message);
			this.spamInfo.put(player, spamInfo);
		}
	}

	private void clearPlayerSpamHistory(RunsafePlayer player)
	{
		if (this.spamInfo.containsKey(player))
		{
			PlayerSpamInfo spamInfo = this.spamInfo.get(player);
			spamInfo.flushHistory();
			this.spamInfo.put(player, spamInfo);
		}
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.spamFilterFloodTimer = iConfiguration.getConfigValueAsInt("spamControl.spamFilterFloodTimer");
		this.spamFilterFloodAmount = iConfiguration.getConfigValueAsInt("spamControl.spamFilterFloodAmount");
		this.spamFilterKickReason = iConfiguration.getConfigValueAsString("spamControl.spamFilterKickReason");
	}

	private HashMap<RunsafePlayer, PlayerSpamInfo> spamInfo;
	private int spamFilterFloodTimer;
	private int spamFilterFloodAmount;
	private String spamFilterKickReason;
	private IScheduler scheduler;
}
