package no.runsafe.nchat.antispam;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.regex.Pattern;

public class IPFilter implements ISpamFilter, IConfigurationChanged
{
	public IPFilter()
	{
		this.IPv4Address = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
	}

	@Override
	public String processString(RunsafePlayer player, String message)
	{
		if (this.isEnabled && this.IPv4Address.matcher(message.replaceAll("\\s*", "")).matches())
		{
			player.sendColouredMessage("&cIt appears you were trying to advertise, I am afraid we do not allow that.");
			return null;
		}
		return message;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		this.isEnabled = config.getConfigValueAsBoolean("antiSpam.enableIPFilter");
	}

	private Pattern IPv4Address;
	private boolean isEnabled;
}
