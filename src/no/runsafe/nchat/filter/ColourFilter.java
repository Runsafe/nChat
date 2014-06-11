package no.runsafe.nchat.filter;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.text.ChatColour;

import javax.annotation.Nullable;

public class ColourFilter implements ISpamFilter
{
	@Nullable
	@Override
	public String processString(IPlayer player, String message)
	{
		return player.hasPermission("runsafe.nchat.colors") ? message : ChatColour.Strip(message);
	}
}
