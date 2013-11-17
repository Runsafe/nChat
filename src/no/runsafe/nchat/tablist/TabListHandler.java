package no.runsafe.nchat.tablist;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerCustomEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.event.player.RunsafeCustomEvent;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.framework.text.ChatColour;

import java.util.Map;

public class TabListHandler implements IPlayerCustomEvent, IConfigurationChanged
{
	private String getTabListName(RunsafePlayer player)
	{
		String firstGroup = (player.isVanished() ? "vanish" : player.getGroups().get(0).toLowerCase());
		String playerName = player.getName();

		if (playerName == null)
			return null;

		String prefix = (prefixes.containsKey(firstGroup)) ? prefixes.get(firstGroup) : "";
		int nameLength = 16 - prefix.length();
		String displayName = (playerName.length() > nameLength) ? playerName.substring(0, nameLength) : playerName;
		return prefix + displayName;
	}

	@Override
	public void OnPlayerCustomEvent(RunsafeCustomEvent event)
	{
		if (event.getEvent().equals("user.group.change"))
			refreshPlayerTabListName(event.getPlayer());
	}

	public void refreshPlayerTabListName(RunsafePlayer player)
	{
		player.setPlayerListName(ChatColour.ToMinecraft(getTabListName(player)));
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		prefixes = configuration.getConfigValuesAsMap("tabListGroupPrefix");
	}

	private Map<String, String> prefixes;
}
