package no.runsafe.nchat.antispam;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DuplicationFilter implements IConfigurationChanged, ISpamFilter
{
	public DuplicationFilter(IScheduler scheduler, IOutput output)
	{
		this.scheduler = scheduler;
		this.output = output;
	}

	@Override
	public String processString(RunsafePlayer player, final String message)
	{
		this.output.write("Duplication filter is processing message");
		if (this.isEnabled)
		{
			this.output.write("We are enabled.");
			final String playerName = player.getName();
			if (this.cooldowns.containsKey(playerName))
			{
				this.output.write("We have a data node for this player");
				if (this.cooldowns.get(playerName).contains(message.toLowerCase()))
				{
					this.output.write("We have seen this message in the past cooldown");
					player.sendColouredMessage("&cYou cannot send the same message that quickly.");
					return null;
				}
			}
			else
			{
				this.output.write("Have not seen player before, making new node.");
				this.cooldowns.put(playerName, new ArrayList<String>());
			}

			this.output.write("Adding message to node and starting timer");
			this.cooldowns.get(playerName).add(message.toLowerCase());

			this.scheduler.startAsyncTask(new Runnable() {
				@Override
				public void run() {
					cooldowns.get(playerName).remove(message.toLowerCase());
					output.write("Timer ran, removing message");
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
	private IOutput output;
}
