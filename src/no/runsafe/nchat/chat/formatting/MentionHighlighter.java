package no.runsafe.nchat.chat.formatting;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;

public class MentionHighlighter implements IChatHighlighter, IConfigurationChanged
{
	@Override
	public String highlight(IPlayer contextPlayer, String message)
	{
		if (!message.contains(highlightChar))
			return message;

		String[] parts = message.split(highlightChar, 2);
		String playerName = contextPlayer.getName();

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
