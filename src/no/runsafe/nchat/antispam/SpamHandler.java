package no.runsafe.nchat.antispam;

import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpamHandler
{
	public SpamHandler(ISpamFilter[] filters)
	{
		this.filters.addAll(Arrays.asList(filters));
	}

	public String getFilteredMessage(RunsafePlayer player, String message)
	{
		for (ISpamFilter filter : this.filters)
			if (message != null)
				message = filter.processString(player, message);

		return message;
	}

	List<ISpamFilter> filters = new ArrayList<ISpamFilter>();
}
