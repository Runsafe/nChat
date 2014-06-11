package no.runsafe.nchat.filter;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.filter.IChatFilter;

import javax.annotation.Nullable;

public class MentionHighlighter implements IChatFilter, IConfigurationChanged
{
	@Nullable
	@Override
	public String processString(IPlayer source, IPlayer target, String message)
	{
		if (!message.contains(highlightChar))
			return message;

		String[] parts = message.split(highlightChar, 2);
		String playerName = target.getName();

		return parts[0] + highlightChar + parts[1].replace(playerName, highlightFormat.replace("#player", playerName));
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		highlightChar = configuration.getConfigValueAsString("chatFormatting.mentionHighlightCharacter");
		highlightFormat = configuration.getConfigValueAsString("chatFormatting.mentionHighlight");
	}

	private String highlightChar;
	private String highlightFormat;
}
