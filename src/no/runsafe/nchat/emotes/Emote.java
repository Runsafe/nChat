package no.runsafe.nchat.emotes;

public class Emote
{
	public Emote(String emote, String singleEmote, String targetEmote)
	{
		this.emote = emote;
		this.singleEmote = formatEmoteString(singleEmote);
		this.targetEmote = formatEmoteString(targetEmote);
	}

	public Emote(String emoteString)
	{
		String[] parts = emoteString.split(",");
		emote = parts[0];
		singleEmote = formatEmoteString(parts[1]);
		targetEmote = formatEmoteString(parts[2]);
	}

	private String formatEmoteString(String emoteString)
	{
		return "&e" + emoteString.replaceAll("%s", "%s&e");
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

	private String emote;
	private String singleEmote;
	private String targetEmote;
}
