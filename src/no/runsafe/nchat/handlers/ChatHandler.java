package no.runsafe.nchat.handlers;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.hook.IPlayerNameDecorator;
import no.runsafe.framework.output.ChatColour;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.Globals;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatHandler implements IConfigurationChanged, IPlayerNameDecorator
{
	public ChatHandler(Globals globals)
	{
		this.globals = globals;
	}

	public String getGroupPrefix(RunsafePlayer player)
	{
		if (!player.getGroups().isEmpty())
		{
			String groupName = player.getGroups().get(0).toLowerCase();
			if (this.chatGroupPrefixes.contains(groupName))
				return (String) this.chatGroupPrefixes.get(groupName);
		}
		return "";
	}

	public String getTabListPrefixedName(RunsafePlayer player)
	{
		String firstGroup = (player.isVanished() ? "vanish" : player.getGroups().get(0).toLowerCase());
		String playerName = player.getName();
		String prefix = (this.tabListPrefixes.contains(firstGroup)) ? (String) this.tabListPrefixes.get(firstGroup) : "";
		int nameLength = 16 - prefix.length();
		String displayName = (playerName.length() > nameLength) ? playerName.substring(0, nameLength) : playerName;

		return prefix + displayName;
	}

	public void refreshPlayerTabListName(RunsafePlayer player)
	{
		player.setPlayerListName(ChatColour.ToMinecraft(this.getTabListPrefixedName(player)));
	}

	public String getWorldPrefix(String worldName)
	{
		String worldPrefix = (String) this.worldPrefixes.get(worldName);

		if (worldPrefix != null)
			return worldPrefix;

		return "";
	}

	public String getPlayerNickname(RunsafePlayer player, String nameString)
	{
		String playerName = player.getName();
		String playerNickname = (String) this.playerNicknames.get(playerName);

		if (playerNickname != null)
			return nameString.replace(playerName, playerNickname);

		return nameString;
	}

	public List<String> getPlayerTags(String playerName)
	{
		List<String> returnTags = new ArrayList<String>();
		List<String> playerTags = this.playerTags.getStringList(playerName);

		if (playerTags != null)
		{
			for (String tag : playerTags)
			{
				returnTags.add(String.format(this.playerTagFormat, tag));
			}
		}

		return returnTags;
	}

	public String formatPlayerName(RunsafePlayer player, String editedName)
	{
		String formatName = this.playerNameFormat;
		String worldName = (player.isOnline() && !player.isVanished() && player.getWorld() != null) ? player.getWorld().getName() : "console";

		HashMap<String, String> replacements = new HashMap<String, String>();

		if (formatName == null) return null;

		replacements.put(Constants.FORMAT_OP, (this.enableOpTag && player.isOP()) ? this.opTagFormat : "");
		replacements.put(Constants.FORMAT_WORLD, (this.enableWorldPrefixes) ? this.getWorldPrefix(worldName) : worldName);
		replacements.put(Constants.FORMAT_GROUP, (this.enableChatGroupPrefixes) ? this.getGroupPrefix(player) : "");
		replacements.put(Constants.FORMAT_TAG, (this.enablePlayerTags) ? this.globals.joinList(this.getPlayerTags(player.getName())) : "");
		replacements.put(Constants.FORMAT_PLAYER_NAME, (this.enableNicknames) ? this.getPlayerNickname(player, editedName) : editedName);

		formatName = this.globals.mapReplace(formatName, replacements);
		return formatName;
	}

	@Override
	public String DecorateName(RunsafePlayer runsafePlayer, String s)
	{
		return this.formatPlayerName(runsafePlayer, s);
	}

	public String formatChatMessage(String message, RunsafePlayer player)
	{
		return this.formatMessage(message, player, this.playerChatMessage);
	}

	public String formatPlayerSystemMessage(String message, RunsafePlayer player)
	{
		return ChatColour.ToMinecraft(this.formatMessage(message, player, this.playerSystemMessage));
	}

	private String formatMessage(String message, RunsafePlayer player, String formatMessage)
	{
		String playerName = this.formatPlayerName(player, player.getName());
		message = message.replace("%", "%%");

		if (!this.enableColorCodes && !player.hasPermission("runsafe.nchat.colors"))
			message = ChatColour.Strip(message);

		formatMessage = formatMessage.replace(Constants.FORMAT_MESSAGE, message);
		formatMessage = formatMessage.replace(Constants.FORMAT_PLAYER_NAME, playerName);

		return formatMessage;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.chatGroupPrefixes = configuration.getSection("chatGroupPrefixes");
		this.worldPrefixes = configuration.getSection("worldPrefixes");
		this.playerNicknames = configuration.getSection("playerNicknames");
		this.playerTags = configuration.getSection("playerTags");
		this.playerTagFormat = configuration.getConfigValueAsString("chatFormatting.playerTagFormat");
		this.playerNameFormat = configuration.getConfigValueAsString("chatFormatting.playerName");
		this.playerChatMessage = configuration.getConfigValueAsString("chatFormatting.playerChatMessage");
		this.playerSystemMessage = configuration.getConfigValueAsString("chatFormatting.playerSystemMessage");
		this.enableWorldPrefixes = configuration.getConfigValueAsBoolean("nChat.enableWorldPrefixes");
		this.enableChatGroupPrefixes = configuration.getConfigValueAsBoolean("nChat.enableChatGroupPrefixes");
		this.enableNicknames = configuration.getConfigValueAsBoolean("nChat.enableNicknames");
		this.enablePlayerTags = configuration.getConfigValueAsBoolean("nChat.enablePlayerTags");
		this.enableOpTag = configuration.getConfigValueAsBoolean("nChat.enableOpTag");
		this.enableColorCodes = configuration.getConfigValueAsBoolean("nChat.enableColorCodes");
		this.opTagFormat = configuration.getConfigValueAsString("chatFormatting.opTagFormat");
		this.tabListPrefixes = configuration.getSection("tabListGroupPrefix");
	}

	private ConfigurationSection tabListPrefixes;
	private Globals globals;
	private boolean enableWorldPrefixes;
	private boolean enableChatGroupPrefixes;
	private boolean enableNicknames;
	private boolean enablePlayerTags;
	private boolean enableOpTag;
	private boolean enableColorCodes;
	private ConfigurationSection chatGroupPrefixes;
	private ConfigurationSection worldPrefixes;
	private ConfigurationSection playerNicknames;
	private ConfigurationSection playerTags;
	private String playerTagFormat;
	private String opTagFormat;
	private String playerChatMessage;
	private String playerSystemMessage;
	private String playerNameFormat;
}
