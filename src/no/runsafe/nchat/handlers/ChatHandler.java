package no.runsafe.nchat.handlers;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.server.RunsafeWorld;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.Globals;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class ChatHandler implements IConfigurationChanged
{
    public ChatHandler(IConfiguration configuration, Globals globals)
    {
        this.configuration = configuration;
        this.globals = globals;
    }

    public String convertColors(String theString)
    {
        for (int i = 0; i < ChatColor.values().length; i++)
        {
            ChatColor theColor = ChatColor.values()[i];
            theString = theString.replaceAll(String.format(Constants.COLOR_CHARACTER, theColor.getChar()), theColor.toString());
        }

        return theString;
    }

    public String stripColors(String theString)
    {
        for (int i = 0; i < ChatColor.values().length; i++)
        {
            ChatColor theColor = ChatColor.values()[i];
            theString = theString.replaceAll(String.format(Constants.COLOR_CHARACTER, theColor.getChar()), "");
        }

        return theString;
    }

    public String getGroupPrefix(RunsafePlayer player)
    {
        ConfigurationSection chatPrefixesSection = this.chatGroupPrefixes;
        Set<String> chatPrefixes = chatPrefixesSection.getKeys(true);
        Iterator<String> chatPrefixesIterator = chatPrefixes.iterator();

        String finalChatPrefix = "";

        while (chatPrefixesIterator.hasNext())
        {
            String chatPrefixID = chatPrefixesIterator.next();

            finalChatPrefix = (String) this.chatGroupPrefixes.get(chatPrefixID);

            if (player.hasPermission("nChat.groupPrefix." + chatPrefixID))
                return finalChatPrefix;
        }

        return finalChatPrefix;
    }

    public String getWorldPrefix(String worldName)
    {
        String worldPrefix = (String) this.worldPrefixes.get(worldName);

        if (worldPrefix != null)
            return worldPrefix;

        return "";
    }

    public String getPlayerNickname(String playerName)
    {
        String playerNickname = (String) this.playerNicknames.get(playerName);

        if (playerNickname != null)
            return playerNickname;

        return playerName;
    }

    public List<String> getPlayerTags(String playerName)
    {
        List<String> returnTags = new ArrayList<String>();
        List<String> playerTags = this.playerTags.getStringList(playerName);

        if (playerTags != null)
        {
            for (Iterator<String> i = playerTags.iterator(); i.hasNext();)
            {
                String tag = i.next();
                returnTags.add(String.format(this.playerTagFormat, tag));
            }
        }

        return returnTags;
    }

    public String formatChatMessage(String message, RunsafePlayer player)
    {
        String formatMessage = this.playerChatMessage;
        RunsafeWorld world = player.getWorld();

        HashMap<String, String> replacements = new HashMap<String, String>();

        if (formatMessage == null)
            return null;

        if (this.enableOpTag && player.isOP())
            replacements.put(Constants.FORMAT_OP, this.opTagFormat);
        else
            replacements.put(Constants.FORMAT_OP, "");

        if (this.enableWorldPrefixes)
            replacements.put(Constants.FORMAT_WORLD, this.getWorldPrefix((world.getName())));
        else
            replacements.put(Constants.FORMAT_WORLD, world.getName());

        if (this.enableChatGroupPrefixes)
            replacements.put(Constants.FORMAT_GROUP, this.getGroupPrefix(player));
        else
            replacements.put(Constants.FORMAT_GROUP, "");

        if (this.enablePlayerTags)
        {
            List<String> playerTags = this.getPlayerTags(player.getName());
            replacements.put(Constants.FORMAT_TAG, this.globals.joinList(playerTags));
        }
        else
        {
            replacements.put(Constants.FORMAT_TAG, "");
        }

        if (this.enableNicknames)
            replacements.put(Constants.FORMAT_PLAYER_NAME, this.getPlayerNickname(player.getName()));
        else
            replacements.put(Constants.FORMAT_PLAYER_NAME, player.getName());

        String chatColor = this.chatColor;
        if (chatColor == null)
            chatColor = "";

        String chatMessage = message;
        if (!player.hasPermission("nChat.allowColorCodes")|| !this.configuration.getConfigValueAsBoolean("nChat.enableColorCodes"))
            chatMessage = this.stripColors(chatMessage);

        replacements.put(Constants.FORMAT_MESSAGE, chatColor + chatMessage);

        formatMessage = this.globals.mapReplace(formatMessage, replacements);
        return this.convertColors(formatMessage).trim();
    }

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		// TODO: Check this does not need to be called on construct
		this.chatGroupPrefixes = configuration.getSection("chatGroupPrefixes");
		this.worldPrefixes = configuration.getSection("worldPrefixes");
		this.playerNicknames = configuration.getSection("playerNicknames");
		this.playerTags = configuration.getSection("playerTags");
		this.playerTagFormat = configuration.getConfigValueAsString("nChat.playerTagFormat");
		this.playerChatMessage = configuration.getConfigValueAsString("chatFormatting.playerChatMessage");
		this.enableWorldPrefixes = configuration.getConfigValueAsBoolean("nChat.enableWorldPrefixes");
		this.enableChatGroupPrefixes = configuration.getConfigValueAsBoolean("nChat.enableChatPrefixes");
		this.enableNicknames = configuration.getConfigValueAsBoolean("nChat.enableNicknames");
		this.enablePlayerTags = configuration.getConfigValueAsBoolean("nChat.enablePlayerTags");
		this.enableOpTag = configuration.getConfigValueAsBoolean("nChat.enableOpTag");
		this.chatColor = this.configuration.getConfigValueAsString("nChat.chatColor");
		this.opTagFormat = this.configuration.getConfigValueAsString("nChat.opTagFormat");
	}

    private Globals globals;
    private boolean enableWorldPrefixes;
    private boolean enableChatGroupPrefixes;
    private boolean enableNicknames;
    private boolean enablePlayerTags;
    private boolean enableOpTag;
    private IConfiguration configuration;
    private ConfigurationSection chatGroupPrefixes;
    private ConfigurationSection worldPrefixes;
    private ConfigurationSection playerNicknames;
    private ConfigurationSection playerTags;
    private String playerTagFormat;
    private String chatColor;
    private String opTagFormat;
    private String playerChatMessage;
}
