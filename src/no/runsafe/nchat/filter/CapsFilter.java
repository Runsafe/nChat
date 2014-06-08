package no.runsafe.nchat.filter;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapsFilter implements ISpamFilter, IConfigurationChanged
{
	public CapsFilter()
	{
		caps = Pattern.compile("[A-Z]");
	}

	@Override
	public String processString(IPlayer player, String message)
	{
		if (isEnabled)
		{
			Matcher matcher = caps.matcher(message);
			int count = 0;
			while (matcher.find()) count++;

			if (count * 100 / WHITESPACE.matcher(message).replaceAll("").length() >= percent)
				return message.toLowerCase();
		}
		return message;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		isEnabled = configuration.getConfigValueAsBoolean("antiSpam.enableCapsFilter");
		percent = configuration.getConfigValueAsInt("antiSpam.capsFilterPercent");
	}

	private boolean isEnabled;
	private int percent;
	private final Pattern caps;
	private static final Pattern WHITESPACE = Pattern.compile("\\s*");
}
