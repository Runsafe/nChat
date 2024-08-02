package no.runsafe.nchat.tablist;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerCustomEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafeCustomEvent;
import no.runsafe.framework.text.ChatColour;

import javax.annotation.Nullable;
import java.util.Map;

public class PlayerTablistNameHandler implements IPlayerCustomEvent, IConfigurationChanged
{

	public static final int MAX_NAME_LENGTH = 16;

	@Nullable
	private String getTabListName(IPlayer player)
	{
		String firstGroup = player.isVanished() ? "vanish" : player.getGroups().get(0).toLowerCase();
		String playerName = player.getName();

		if (playerName == null)
			return null;

		// Don't allow fake znpcs to show up on the tab list
		if (playerName.contains("[ZNPC]"))
			return null;

		String prefix = prefixes.getOrDefault(firstGroup, "");
		int nameLength = MAX_NAME_LENGTH - prefix.length();
		String displayName = playerName.length() > nameLength ? playerName.substring(0, nameLength) : playerName;
		return prefix + displayName;
	}

	@Override
	public void OnPlayerCustomEvent(RunsafeCustomEvent event)
	{
		if (event.getEvent().equals("user.group.change"))
			refreshPlayerTabListName(event.getPlayer());
	}

	public void refreshPlayerTabListName(IPlayer player)
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
