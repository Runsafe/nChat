package no.runsafe.nchat.handlers;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;
import no.runsafe.nchat.PlayerSpamInfo;

import java.util.HashMap;

public class SpamHandler implements IConfigurationChanged
{
	public SpamHandler(IScheduler scheduler, IOutput console)
	{
		this.spamInfo = new HashMap<String, PlayerSpamInfo>();
		this.console = console;
		this.scheduler = scheduler;
	}

	public void checkForSpam(RunsafePlayer player, String message)
	{
		final String playerName = player.getName();
		if (this.spamInfo.containsKey(playerName))
		{
			PlayerSpamInfo playerSpamInfo = this.spamInfo.get(playerName);

			if (!playerSpamInfo.isFloodTimerRunning())
			{
				this.scheduler.startAsyncTask(new Runnable()
				{
					@Override
					public void run()
					{
						clearPlayerSpamHistory(playerName);
					}
				}, this.spamFilterFloodTimer);
				playerSpamInfo.startFloodTimer();
			}
			playerSpamInfo.addMessageToHistory(message);

			if (playerSpamInfo.currentFloodCount() == spamFilterFloodAmount)
			{
				this.console.write("Kicking " + playerName + " for flooding chat with spam.");
				player.kick(this.spamFilterKickReason);
			}
		}
		else
		{
			PlayerSpamInfo spamInfo = new PlayerSpamInfo();
			spamInfo.addMessageToHistory(message);
			this.spamInfo.put(player.getName(), spamInfo);
		}
	}

	private void clearPlayerSpamHistory(String playerName)
	{
		if (this.spamInfo.containsKey(playerName))
		{
			PlayerSpamInfo spamInfo = this.spamInfo.get(playerName);
			spamInfo.flushHistory();
			this.spamInfo.put(playerName, spamInfo);
		}
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.spamFilterFloodTimer = iConfiguration.getConfigValueAsInt("spamControl.spamFilterFloodTimer");
		this.spamFilterFloodAmount = iConfiguration.getConfigValueAsInt("spamControl.spamFilterFloodAmount");
		this.spamFilterKickReason = iConfiguration.getConfigValueAsString("spamControl.spamFilterKickReason");
		this.spamFilterRepeatAmount = iConfiguration.getConfigValueAsInt("spamControl.spamFilterRepeatAmount");
		this.spamFilterRepeatTimer = iConfiguration.getConfigValueAsInt("spamControl.spamFilterRepeatTimer");
	}

	private HashMap<String, PlayerSpamInfo> spamInfo;
	private int spamFilterFloodTimer;
	private int spamFilterFloodAmount;
	private int spamFilterRepeatTimer;
	private int spamFilterRepeatAmount;
	private String spamFilterKickReason;
	private IScheduler scheduler;
	private IOutput console;
}
