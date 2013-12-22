package no.runsafe.nchat.antispam;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class IPFilter implements ISpamFilter, IConfigurationChanged
{
	private static final Pattern WHITESPACE = Pattern.compile("\\s*");

	public IPFilter()
	{
		IPv4Address = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
	}

	@Nullable
	@Override
	public String processString(IPlayer player, String message)
	{
		if (isEnabled && IPv4Address.matcher(WHITESPACE.matcher(message).replaceAll("")).find())
		{
			player.sendColouredMessage("&cResorting to advertising your server on other servers is pathetic.");
			return null;
		}
		return message;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		isEnabled = configuration.getConfigValueAsBoolean("antiSpam.enableIPFilter");
	}

	private final Pattern IPv4Address;
	private boolean isEnabled;
}
