package no.runsafe.nchat.antispam;

import no.runsafe.framework.api.player.IPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class SpamHandler
{
	public SpamHandler(ISpamFilter... filters)
	{
		this.filters.addAll(Arrays.asList(filters));
	}

	public String getFilteredMessage(IPlayer player, String message)
	{
		for (ISpamFilter filter : filters)
			if (message != null)
				message = filter.processString(player, message);

		return message;
	}

	private final Collection<ISpamFilter> filters = new ArrayList<ISpamFilter>(0);
}
