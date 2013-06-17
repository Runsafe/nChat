package no.runsafe.nchat.antispam;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.concurrent.ConcurrentHashMap;

public class FloodFilter implements ISpamFilter, IConfigurationChanged
{
	public FloodFilter(IScheduler scheduler)
	{
		this.scheduler = scheduler;
	}

	@Override
	public String processString(RunsafePlayer player, String message)
	{
		if (this.isEnabled)
		{
			final String playerName = player.getName();
			if (this.floodChecks.containsKey(playerName))
			{
				int currentAmount = this.floodChecks.get(playerName);
				if (currentAmount >= this.threshold)
				{
					player.sendColouredMessage("&cYou are talking too fast, slow down.");
					return null;
				}
				else
				{
					this.floodChecks.put(playerName, currentAmount + 1);
				}
			}
			else
			{
				this.floodChecks.put(playerName, 1);
				this.scheduler.startAsyncTask(new Runnable() {
					@Override
					public void run() {
						floodChecks.remove(playerName);
					}
				}, this.period);
			}
		}
		return message;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		this.isEnabled = config.getConfigValueAsBoolean("enableFloodFilter");
		this.threshold = config.getConfigValueAsInt("floodFilterThreshold");
		this.period = config.getConfigValueAsInt("floodFilterPeriod");
	}

	private boolean isEnabled;
	private int threshold;
	private int period;
	private ConcurrentHashMap<String, Integer> floodChecks = new ConcurrentHashMap<String, Integer>();
	private IScheduler scheduler;
}
