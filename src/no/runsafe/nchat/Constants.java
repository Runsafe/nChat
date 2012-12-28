package no.runsafe.nchat;

import org.bukkit.ChatColor;

public class Constants
{
    public static String DEFAULT_CONFIGURATION_FILE = "defaultConfig.yml";
    public static String CONFIGURATION_FILE = "plugins/nChat/config.yml";

    public static String COLOR_CHARACTER = "&%s";

    public static String FORMAT_PLAYER_NAME = "#name";
    public static String FORMAT_WORLD = "#world";
    public static String FORMAT_GROUP = "#group";
    public static String FORMAT_TAG = "#tags";
    public static String FORMAT_MESSAGE = "#message";
    public static String FORMAT_OP = "#op";
    public static String FORMAT_CHANNEL = "#channel";

    public static ChatColor DEFAULT_MESSAGE_COLOR = ChatColor.AQUA;

    public static String CHAT_CHANNEL_NODE = "nChat.channel.%s";

    public static String CHANNEL_NOT_EXIST = "The specified channel does not exist.";
    public static String CHANNEL_NO_PERMISSION = "You do not have permission to speak in this channel.";

    public static String CHAT_MUTED = "Messages are currently not being broadcast.";

    public static String COMMAND_CHAT_MUTED = "Global chat has now been muted.";
    public static String COMMAND_CHAT_UNMUTED = "Global chat has now been unmuted";
}
