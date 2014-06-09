package no.runsafe.nchat.chat.formatting;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.hook.IPlayerNameDecorator;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.api.player.IPlayerPermissions;
import no.runsafe.framework.minecraft.player.RunsafeFakePlayer;
import no.runsafe.framework.text.ChatColour;
import no.runsafe.nchat.channel.IChatChannel;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ChatFormatter implements IPlayerNameDecorator, IConfigurationChanged
{
	public ChatFormatter(RegionHandler regionHandler)
	{
		this.regionHandler = regionHandler;
	}

	@Nullable
	public static String mapReplace(String theString, HashMap<String, String> replaceMap)
	{
		if (theString == null)
			return null;
		for (Map.Entry<String, String> entry : replaceMap.entrySet())
			theString = theString.replaceAll(entry.getKey(), entry.getValue());
		return theString;
	}

	public String formatChatMessage(IPlayer player, String message)
	{
		HashMap<String, String> replacements = new HashMap<String, String>(2);

		if (disableColorCodes && !player.hasPermission("runsafe.nchat.colors"))
			message = ChatColour.Strip(message);

		replacements.put("#player", formatPlayerName(player, player.getName()));
		replacements.put("#message", SPECIAL_CHARACTER.matcher(message.trim()).replaceAll("\\\\$1"));

		return mapReplace(playerChatMessage, replacements);
	}

	public String formatPrivateMessageLog(ICommandExecutor you, ICommandExecutor player, String message)
	{
		return messageLogFormat
			.replace("#source", getName(you))
			.replace("#target", getName(player))
			.replace("#message", message);
	}

	public String formatPrivateMessageTo(ICommandExecutor you, ICommandExecutor player, String message)
	{
		return messageOutFormat
			.replace("#source", getName(you))
			.replace("#target", getName(player))
			.replace("#message", message);
	}

	public String formatPrivateMessageFrom(ICommandExecutor you, ICommandExecutor player, String message)
	{
		return messageInFormat
			.replace("#source", getName(you))
			.replace("#target", getName(player))
			.replace("#message", message);
	}

	public String formatMessage(ICommandExecutor player, IChatChannel channel, String message)
	{
		return channelFormat
			.replace("#tag", getChannelTag(channel))
			.replace("#player", getName(player))
			.replace("#message", message);
	}

	private String getName(ICommandExecutor executor)
	{
		return executor instanceof IPlayer ? ((IPlayer) executor).getPrettyName() : executor.getName();
	}

	private String getChannelTag(IChatChannel channel)
	{
		String tag = channel.getCustomTag();
		if(tag != null)
			return tag;
		tag = channel.getName();
		return channelTags.containsKey(tag) ? channelTags.get(tag) : tag;
	}

	private String getWorldPrefix(IPlayer player, String worldName)
	{
		if (player.isOnline() && player.isDead() && worldPrefixes.containsKey("dead"))
			return worldPrefixes.get("dead");

		if (worldPrefixes.containsKey(worldName))
			return worldPrefixes.get(worldName);

		return "";
	}

	public String getGroupPrefix(IPlayerPermissions player)
	{
		if (!player.getGroups().isEmpty())
		{
			String groupName = player.getGroups().get(0).toLowerCase();
			Map<String, String> prefixes = chatGroupPrefixes;
			if (prefixes.containsKey(groupName))
				return prefixes.get(groupName);
		}
		return "";
	}

	@Nullable
	public String formatPlayerName(IPlayer player, String name)
	{
		String formatName = playerNameFormat;
		if (formatName == null) return null;

		String worldName = player.isOnline() && !player.isVanished() && player.getWorld() != null ? player.getWorldName() : "console";

		HashMap<String, String> replacements = new HashMap<String, String>(2);
		replacements.put("#op", player.isOP() ? opTagFormat : "");
		replacements.put("#ban", player.isNotBanned() ? "" : banTagFormat);

		if (enableRegionPrefixes && !(player instanceof RunsafeFakePlayer) && player.isOnline() && !worldName.equals("console"))
		{
			String regionTag = regionHandler.getRegionTag(player);
			worldName = regionTag != null ? regionTag : getWorldPrefix(player, worldName);
		}
		else
		{
			worldName = getWorldPrefix(player, worldName);
		}

		replacements.put("#world", worldName);
		replacements.put("#group", getGroupPrefix(player));
		replacements.put("#player", name);

		formatName = mapReplace(formatName, replacements);
		return formatName;
	}

	@Override
	public String DecorateName(IPlayer player, String name)
	{
		return formatPlayerName(player, name);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		chatGroupPrefixes = configuration.getConfigValuesAsMap("chatGroupPrefixes");
		worldPrefixes = configuration.getConfigValuesAsMap("worldPrefixes");
		opTagFormat = configuration.getConfigValueAsString("chatFormatting.opTagFormat");
		banTagFormat = configuration.getConfigValueAsString("chatFormatting.banTagFormat");
		playerNameFormat = configuration.getConfigValueAsString("chatFormatting.playerName");
		playerChatMessage = configuration.getConfigValueAsString("chatFormatting.playerChatMessage");
		enableRegionPrefixes = configuration.getConfigValueAsBoolean("nChat.enableRegionPrefixes");
		disableColorCodes = !configuration.getConfigValueAsBoolean("nChat.enableColorCodes");
		channelFormat = configuration.getConfigValueAsString("chatMessage.channel");
		messageInFormat = configuration.getConfigValueAsString("chatMessage.whisperFrom");
		messageOutFormat = configuration.getConfigValueAsString("chatMessage.whisperTo");
		messageLogFormat = configuration.getConfigValueAsString("chatMessage.whisperLog");
		channelTags = configuration.getConfigValuesAsMap("channelTags");
	}

	private Map<String, String> chatGroupPrefixes;
	private Map<String, String> worldPrefixes;
	private Map<String, String> channelTags;
	private String opTagFormat;
	private String banTagFormat;
	private String playerChatMessage;
	private String playerNameFormat;
	private String channelFormat;
	private String messageOutFormat;
	private String messageInFormat;
	private String messageLogFormat;
	private boolean disableColorCodes;
	private boolean enableRegionPrefixes;

	private final RegionHandler regionHandler;
	private static final Pattern SPECIAL_CHARACTER = Pattern.compile("([\\\\$])");
}
