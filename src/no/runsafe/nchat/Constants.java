package no.runsafe.nchat;

import no.runsafe.framework.output.ChatColour;

public class Constants
{
//	public static String COLOR_CHARACTER = "&%s";

	public static final String FORMAT_PLAYER_NAME = "#player";
	public static final String FORMAT_KILLER = "#killer";
	public static final String FORMAT_WORLD = "#world";
	public static final String FORMAT_GROUP = "#group";
	public static final String FORMAT_TAG = "#tags";
	public static final String FORMAT_MESSAGE = "#message";
	public static final String FORMAT_OP = "#op";
	public static final String FORMAT_CHANNEL = "#channel";

	public static final ChatColour DEFAULT_MESSAGE_COLOR = ChatColour.AQUA;

	public static final String CHAT_CHANNEL_NODE = "nChat.channel.%s";

	public static final String CHANNEL_NOT_EXIST = "The specified channel does not exist.";
	public static final String CHANNEL_NO_PERMISSION = "You do not have permission to speak in this channel.";

	public static final String CHAT_MUTED = "&cYou cannot broadcast messages right now.";

	public static final String COMMAND_CHAT_MUTED = "Global chat has now been muted.";
	public static final String COMMAND_CHAT_UNMUTED = "Global chat has now been unmuted";

	public static final String COMMAND_ENTER_PLAYER = "Please enter a player name";
	public static final String COMMAND_NO_PERMISSION = "&cYou do not have permission to do that.";
	public static final String COMMAND_TARGET_EXEMPT = "&cYou cannot use that on the specified player.";
	public static final String COMMAND_TARGET_NO_EXISTS = "&cThat player does not exist.";

	public static final String WHISPER_NO_TARGET = "&cThe player %s does not exist.";
	public static final String WHISPER_TARGET_OFFLINE = "&cThe player %s is currently offline.";
	public static final String WHISPER_NO_REPLY_TARGET = "&cYou have nothing to reply to.";
}
