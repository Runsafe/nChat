package no.runsafe.nchat.antispam;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapsFilter implements ISpamFilter, IConfigurationChanged
{
	public CapsFilter()
	{
		this.caps = Pattern.compile("[A-Z]{1}");
	}

	@Override
	public String processString(RunsafePlayer player, String message)
	{
		if (this.isEnabled)
		{
			Matcher matcher = this.caps.matcher(message);
			if (matcher.matches())
			{
				int count = 0;
				while (matcher.find()) count++;

				if ((count * 100) / message.length() >= this.percent)
					return message.toLowerCase();
			}
		}
		return message;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		this.isEnabled = config.getConfigValueAsBoolean("enableCapsFilter");
		this.percent = config.getConfigValueAsInt("capsFilterPercent");
	}

	private boolean isEnabled;
	private int percent;
	private Pattern caps;
}
