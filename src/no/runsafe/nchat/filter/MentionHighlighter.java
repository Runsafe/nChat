package no.runsafe.nchat.filter;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.text.ChatColour;

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

		String[] parts = message.split(highlightChar, 2);

		return parts[0] + highlightChar + parts[1].replace(playerName, highlightFormat.replace("#player", playerName));
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		highlightChar = configuration.getConfigValueAsString("chatFormatting.mentionHighlightCharacter");
		highlightFormat = ChatColour.ToMinecraft(configuration.getConfigValueAsString("chatFormatting.mentionHighlight"));
	}

	private String highlightChar;
	private String highlightFormat;
}
