package no.runsafe.nchat.filter;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;

import javax.annotation.Nullable;

public class MentionHighlighter implements IChatFilter, IConfigurationChanged
{
	@Nullable
	@Override
	public String processString(IPlayer source, IPlayer target, String message)
	{
		String playerName = target.getName();
		if (!message.contains(playerName))
			return message;

		return message.replace(playerName, highlightFormat.replace("#player", playerName));
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		highlightFormat = configuration.getConfigValueAsString("chatFormatting.mentionHighlight");
	}

	private String highlightFormat;
}
