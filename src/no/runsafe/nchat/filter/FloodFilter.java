package no.runsafe.nchat.filter;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

public class FloodFilter implements ISpamFilter, IConfigurationChanged
{
	public FloodFilter(IScheduler scheduler)
	{
		this.scheduler = scheduler;
	}

	@Nullable
	@Override
	public String processString(IPlayer player, String message)
	{
		if (isEnabled)
		{
			final String playerName = player.getName();
			if (floodChecks.containsKey(playerName))
			{
				int currentAmount = floodChecks.get(playerName);
				if (currentAmount >= threshold)
				{
					player.sendColouredMessage("&cYou are talking too fast, slow down.");
					return null;
				}
				else
				{
					floodChecks.put(playerName, currentAmount + 1);
				}
			}
			else
			{
				floodChecks.put(playerName, 1);
				scheduler.startAsyncTask(new Runnable()
				{
					@Override
					public void run()
					{
						floodChecks.remove(playerName);
					}
				}, period);
			}
		}
		return message;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		isEnabled = configuration.getConfigValueAsBoolean("antiSpam.enableFloodFilter");
		threshold = configuration.getConfigValueAsInt("antiSpam.floodFilterThreshold");
		period = configuration.getConfigValueAsInt("antiSpam.floodFilterPeriod");
	}

	private boolean isEnabled;
	private int threshold;
	private int period;
	private final ConcurrentHashMap<String, Integer> floodChecks = new ConcurrentHashMap<String, Integer>();
	private final IScheduler scheduler;
}
