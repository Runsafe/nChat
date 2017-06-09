package no.runsafe.nchat.filter;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DuplicationFilter implements IConfigurationChanged, ISpamFilter
{
	public DuplicationFilter(IScheduler scheduler)
	{
		this.scheduler = scheduler;
	}

	@Nullable
	@Override
	public String processString(IPlayer player, final String message)
	{
		if (isEnabled)
		{
			final String playerName = player.getName();
			if (cooldowns.containsKey(playerName))
			{
				if (cooldowns.get(playerName).contains(message.toLowerCase()))
				{
					player.sendColouredMessage("&cYou cannot send the same message that quickly.");
					return null;
				}
			}
			else
			{
				cooldowns.put(playerName, new ArrayList<>(1));
			}

			cooldowns.get(playerName).add(message.toLowerCase());

			scheduler.startAsyncTask(new Runnable()
			{
				@Override
				public void run()
				{
					cooldowns.get(playerName).remove(message.toLowerCase());
					if (cooldowns.get(playerName).isEmpty())
						cooldowns.remove(playerName);
				}
			}, cooldown);
		}
		return message;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		isEnabled = configuration.getConfigValueAsBoolean("antiSpam.enableDuplicationFilter");
		cooldown = configuration.getConfigValueAsInt("antiSpam.duplicationFilterCooldown");
	}

	private boolean isEnabled;
	private int cooldown;
	private final ConcurrentHashMap<String, List<String>> cooldowns = new ConcurrentHashMap<>();
	private final IScheduler scheduler;
}
