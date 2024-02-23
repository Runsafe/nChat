package no.runsafe.nchat.filter;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;

import java.util.regex.Pattern;

public class CapsFilter implements ISpamFilter, IConfigurationChanged
{
	@Override
	public String processString(IPlayer player, String message)
	{
		if (!isEnabled)
			return message;

		String check = IGNORE.matcher(message).replaceAll("");
		String uppercaseOnly = NONCAPS.matcher(check).replaceAll("");

		if (!check.isEmpty() && uppercaseOnly.length() * 100 / check.length() >= percent)
			return message.toLowerCase();

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
	private static final Pattern IGNORE = Pattern.compile("[^A-Za-z]+");
	private static final Pattern NONCAPS = Pattern.compile("[^A-Z]+");
}
