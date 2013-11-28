package no.runsafe.nchat.chat.formatting;

import no.runsafe.framework.api.hook.IPlayerNameDecorator;
import no.runsafe.framework.minecraft.player.RunsafeFakePlayer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.framework.text.ChatColour;
import no.runsafe.nchat.Utils;

import java.util.HashMap;
import java.util.Map;

public class ChatFormatter implements IPlayerNameDecorator
{
	public ChatFormatter(FormattingConfig config, RegionHandler regionHandler)
	{
		this.config = config;
		this.regionHandler = regionHandler;
	}

	public String formatChatMessage(RunsafePlayer player, String message)
	{
		HashMap<String, String> replacements = new HashMap<String, String>();

		if (!(config.colorCodesEnabled() || player.hasPermission("runsafe.nchat.colors")))
			message = ChatColour.Strip(message);

		replacements.put("#player", formatPlayerName(player, player.getName()));
		replacements.put("#message", message.trim().replace("([\\$])", "\\\1"));

		return Utils.mapReplace(config.getPlayerChatFormat(), replacements);
	}

	private String getWorldPrefix(String worldName)
	{
		Map<String, String> prefixes = config.getWorldPrefixes();
		if (prefixes.containsKey(worldName))
			return prefixes.get(worldName);

		return "";
	}

	public String getGroupPrefix(RunsafePlayer player)
	{
		if (!player.getGroups().isEmpty())
		{
			String groupName = player.getGroups().get(0).toLowerCase();
			Map<String, String> prefixes = config.getChatGroupPrefixes();
			if (prefixes.containsKey(groupName))
				return prefixes.get(groupName);
		}
		return "";
	}

	public String formatPlayerName(RunsafePlayer player, String name)
	{
		String formatName = config.getPlayerNameFormat();
		if (formatName == null) return null;

		String worldName = (player.isOnline() && !player.isVanished() && player.getWorld() != null) ? player.getWorldName() : "console";

		HashMap<String, String> replacements = new HashMap<String, String>();
		replacements.put("#op", player.isOP() ? config.getOpTagFormat() : "");
		replacements.put("#ban", !player.isNotBanned() ? config.getBanTagFormat() : "");

		if (config.regionPrefixesEnabled() && !(player instanceof RunsafeFakePlayer) && player.isOnline() && !worldName.equals("console"))
		{
			String regionTag = regionHandler.getRegionTag(player);
			worldName = (regionTag != null ? regionTag : getWorldPrefix(worldName));
		}
		else
		{
			worldName = getWorldPrefix(worldName);
		}

		replacements.put("#world", worldName);
		replacements.put("#group", this.getGroupPrefix(player));
		replacements.put("#player", name);

		formatName = Utils.mapReplace(formatName, replacements);
		return formatName;
	}

	@Override
	public String DecorateName(RunsafePlayer runsafePlayer, String name)
	{
		return formatPlayerName(runsafePlayer, name);
	}

	private final FormattingConfig config;
	private final RegionHandler regionHandler;
}
