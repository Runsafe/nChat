package no.runsafe.nchat.antispam;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;

import java.util.regex.Pattern;

public class IPFilter implements ISpamFilter, IConfigurationChanged
{
	public IPFilter()
	{
		this.IPv4Address = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
	}

	@Override
	public String processString(IPlayer player, String message)
	{
		if (this.isEnabled && this.IPv4Address.matcher(message.replaceAll("\\s*", "")).find())
		{
			player.sendColouredMessage("&cResorting to advertising your server on other servers is pathetic.");
			return null;
		}
		return message;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		this.isEnabled = config.getConfigValueAsBoolean("antiSpam.enableIPFilter");
	}

	private final Pattern IPv4Address;
	private boolean isEnabled;
}
