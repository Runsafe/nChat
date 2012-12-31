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
		this.playerStrikes = new HashMap<String, Integer>();
	}

	public void checkForSpam(RunsafePlayer player, String message)
	{
		if (this.enableSpamControl)
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
							resetPlayerFlooding(playerName);
						}
					}, this.spamFilterFloodTimer);
					playerSpamInfo.startFloodTimer();
				}

				if (!playerSpamInfo.isRepeatTimerRunning())
				{
					this.scheduler.startAsyncTask(new Runnable()
					{
						@Override
						public void run()
						{
							resetPlayerRepeating(playerName);
						}
					}, this.spamFilterRepeatTimer);
					playerSpamInfo.startRepeatTimer();
				}

				playerSpamInfo.setLastMessage(message);

				if (playerSpamInfo.currentFloodCount() == spamFilterFloodAmount || playerSpamInfo.currentRepeatCount() == spamFilterRepeatAmount)
					this.kickPlayer(player);
			}
			else
			{
				PlayerSpamInfo spamInfo = new PlayerSpamInfo();
				spamInfo.setLastMessage(message);
				this.spamInfo.put(player.getName(), spamInfo);
			}
		}
	}

	private void kickPlayer(RunsafePlayer player)
	{
		String playerName = player.getName();

		if (this.getStrikes(playerName) == this.spamFilterKicksBeforeBan)
		{
			this.console.write("Banning " + playerName + " for spam.");
			player.setBanned(true);
			player.kick(this.spamFilterBanReason);
		}
		else
		{
			this.console.write("Kicking " + playerName + " for spam.");
			this.addStrike(playerName);
			player.kick(this.spamFilterKickReason);
		}
	}

	private void addStrike(String playerName)
	{
		int strikes = (this.playerStrikes.containsKey(playerName)) ? this.playerStrikes.get(playerName) + 1 : 0;
		this.playerStrikes.put(playerName, strikes);
	}

	private int getStrikes(String playerName)
	{
		return (this.playerStrikes.containsKey(playerName)) ? this.playerStrikes.get(playerName) : 0;
	}

	private void resetPlayerFlooding(String playerName)
	{
		if (this.spamInfo.containsKey(playerName))
		{
			PlayerSpamInfo spamInfo = this.spamInfo.get(playerName);
			spamInfo.resetFloodCount();
			this.spamInfo.put(playerName, spamInfo);
		}
	}

	private void resetPlayerRepeating(String playerName)
	{
		if (this.spamInfo.containsKey(playerName))
		{
			PlayerSpamInfo spamInfo = this.spamInfo.get(playerName);
			spamInfo.resetPlayerRepeating();
			this.spamInfo.put(playerName, spamInfo);
		}
	}

	public void flushPlayer(RunsafePlayer player)
	{
		String playerName = player.getName();
		if (this.spamInfo.containsKey(playerName))
			this.spamInfo.remove(playerName);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.spamFilterFloodTimer = iConfiguration.getConfigValueAsInt("spamControl.spamFilterFloodTimer");
		this.spamFilterFloodAmount = iConfiguration.getConfigValueAsInt("spamControl.spamFilterFloodAmount");
		this.spamFilterKickReason = iConfiguration.getConfigValueAsString("spamControl.spamFilterKickReason");
		this.spamFilterRepeatAmount = iConfiguration.getConfigValueAsInt("spamControl.spamFilterRepeatAmount");
		this.spamFilterRepeatTimer = iConfiguration.getConfigValueAsInt("spamControl.spamFilterRepeatTimer");
		this.spamFilterBanReason = iConfiguration.getConfigValueAsString("spamControl.spamFilterBanReason");
		this.spamFilterKicksBeforeBan = iConfiguration.getConfigValueAsInt("spamControl.spamFilterKicksBeforeBan");
		this.enableSpamControl = iConfiguration.getConfigValueAsBoolean("nChat.enableSpamControl");
	}

	private HashMap<String, PlayerSpamInfo> spamInfo;
	private HashMap<String, Integer> playerStrikes;
	private int spamFilterFloodTimer;
	private int spamFilterFloodAmount;
	private int spamFilterRepeatTimer;
	private int spamFilterRepeatAmount;
	private int spamFilterKicksBeforeBan;
	private String spamFilterKickReason;
	private boolean enableSpamControl;
	private String spamFilterBanReason;
	private IScheduler scheduler;
	private IOutput console;
}
