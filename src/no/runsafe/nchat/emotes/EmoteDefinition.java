package no.runsafe.nchat.emotes;

import java.util.regex.Pattern;

public class EmoteDefinition
{
	private static final Pattern FIELD_SEPARATOR = Pattern.compile(",");
	private static final Pattern FORMAT_TOKEN = Pattern.compile("%s");

	public EmoteDefinition(CharSequence emoteString)
	{
		String[] parts = FIELD_SEPARATOR.split(emoteString);
		emote = parts[0];
		singleEmote = formatEmoteString(parts[1]);
		targetEmote = formatEmoteString(parts[2]);
	}

	private static String formatEmoteString(CharSequence emoteString)
	{
		return "&e" + FORMAT_TOKEN.matcher(emoteString).replaceAll("%s&e");
	}

	public String getEmote()
	{
		return emote;
	}

	public String getSingleEmote()
	{
		return singleEmote;
	}

	public String getTargetEmote()
	{
		return targetEmote;
	}

	private final String emote;
	private final String singleEmote;
	private final String targetEmote;
}
