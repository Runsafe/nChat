package no.runsafe.nchat.channel;

import no.runsafe.framework.api.event.player.IPlayerCommandPreprocessEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerCommandPreprocessEvent;
import no.runsafe.nchat.chat.InternalRealChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChannelSelector implements IPlayerCommandPreprocessEvent
{
	public ChannelSelector(IChannelManager manager)
	{
		this.manager = manager;
	}

	@Override
	public void OnBeforePlayerCommand(RunsafePlayerCommandPreprocessEvent event)
	{
		Matcher indexMatcher = INDEX_SELECTOR.matcher(event.getMessage());
		if (!indexMatcher.matches())
			return;

		IChatChannel channel = manager.getChannelByIndex(event.getPlayer(), Integer.parseInt(indexMatcher.group(1)));
		if (channel == null)
			return;
		event.cancel();
		manager.setDefaultChannel(event.getPlayer(), channel);
		if (indexMatcher.groupCount() > 2)
		{
			new InternalRealChatEvent(event.getPlayer(), indexMatcher.group(3)).Fire();
		}
	}

	private final IChannelManager manager;
	private static final Pattern INDEX_SELECTOR = Pattern.compile("^/(\\d{1,2})( (.*)|)");
}
