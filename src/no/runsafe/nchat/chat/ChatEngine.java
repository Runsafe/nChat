package no.runsafe.nchat.chat;

import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.antispam.SpamHandler;
import no.runsafe.nchat.chat.formatting.ChatFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChatEngine
{
	public ChatEngine(ChatFormatter chatFormatter, MuteHandler muteHandler, SpamHandler spamHandler)
	{
		this.chatFormatter = chatFormatter;
		this.muteHandler = muteHandler;
		this.spamHandler = spamHandler;
	}

	/**
	 * Used to send a broadcast from a player, will be subject to mute and spam checks.
	 * Returns true if the message was broadcast successfully, otherwise false.
	 *
	 * @param player The player to broadcast the message.
	 * @param message The message the player will broadcast.
	 */
	public boolean playerBroadcast(RunsafePlayer player, String message)
	{
		if (!muteCheck(player))
			return false;

		String filteredMessage = spamHandler.getFilteredMessage(player, message);

		if (filteredMessage != null)
			broadcastMessageAsPlayer(player, message);

		return false;
	}

	/**
	 * Used to broadcast a system message from a player, will be subject to mute and spam checks.
	 * Returns true if the message was broadcast successfully, otherwise false.
	 *
	 * @param player The player to broadcast the message.
	 * @param message The message the player will broadcast.
	 * @return boolean
	 */
	public boolean playerSystemBroadcast(RunsafePlayer player, String message)
	{
		if (!muteCheck(player))
			return false;

		broadcastMessage(message);
		return true;
	}

	/**
	 * Broadcast a message from a player, not subject to any spam or mute checks.
	 *
	 * @param player The player to broadcast the message.
	 * @param message The message the player will broadcast.
	 */
	public void broadcastMessageAsPlayer(RunsafePlayer player, String message)
	{
		broadcastMessage(chatFormatter.formatChatMessage(player, message));
	}

	/**
	 * Broadcast a message to the server.
	 *
	 * @param message The message to broadcast.
	 */
	public void broadcastMessage(String message)
	{
		broadcastMessage(message, null);
	}

	/**
	 * Broadcast a message to the server.
	 *
	 * @param message The message to be broadcast.
	 * @param excludedPlayers A list of players who will not see this message.
	 */
	public void broadcastMessage(String message, List<String> excludedPlayers)
	{
		excludedPlayers = excludedPlayers == null ? new ArrayList<String>() : excludedPlayers;
		List<RunsafePlayer> worldPlayers = RunsafeServer.Instance.getOnlinePlayers();

		for (RunsafePlayer worldPlayer : worldPlayers)
			if (!excludedPlayers.contains(worldPlayer.getName()))
				worldPlayer.sendColouredMessage(message);
	}

	/**
	 * A mute check.
	 *
	 * @param player The player to check.
	 * @return False if the player us muted.
	 */
	private boolean muteCheck(RunsafePlayer player)
	{
		// Mute check.
		if (muteHandler.isPlayerMuted(player))
		{
			player.sendColouredMessage("&cYou cannot broadcast messages right now.");
			return false;
		}
		return true;
	}

	private final ChatFormatter chatFormatter;
	private final MuteHandler muteHandler;
	private final SpamHandler spamHandler;
}
