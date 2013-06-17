package no.runsafe.nchat.antispam;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DuplicationFilter implements IConfigurationChanged, ISpamFilter
{
	public DuplicationFilter(IScheduler scheduler)
	{
		this.scheduler = scheduler;
	}

	@Override
	public String processString(RunsafePlayer player, final String message)
	{
		if (this.isEnabled)
		{
			final String playerName = player.getName();
			if (this.cooldowns.containsKey(playerName))
			{
				if (this.cooldowns.get(playerName).contains(message.toLowerCase()))
				{
					player.sendColouredMessage("&cYou cannot send the same message that quickly.");
					return null;
				}
			}

			List<String> messages = new ArrayList<String>();
			messages.add(message.toLowerCase());
			this.cooldowns.put(playerName, messages);

			this.scheduler.startAsyncTask(new Runnable() {
				@Override
				public void run() {
					cooldowns.get(playerName).remove(message.toLowerCase());
				}
			}, this.cooldown);
		}
		return message;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		this.isEnabled = config.getConfigValueAsBoolean("enableDuplicationFilter");
		this.cooldown = config.getConfigValueAsInt("duplicationFilterCooldown");
	}

	private boolean isEnabled;
	private int cooldown;
	private ConcurrentHashMap<String, List<String>> cooldowns = new ConcurrentHashMap<String, List<String>>();
	private IScheduler scheduler;
}
