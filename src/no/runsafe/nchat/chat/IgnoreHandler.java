package no.runsafe.nchat.chat;

import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.database.IgnoreDatabase;

import java.util.HashMap;
import java.util.List;

public class IgnoreHandler
{
	public IgnoreHandler(IgnoreDatabase database)
	{
		this.database = database;
		ignoreList = database.getIgnoreList();
	}

	/**
	 * Adds a player to the players ignore list.
	 *
	 * @param player The player who will ignore.
	 * @param ignorePlayer The player to be ignored.
	 */
	public void ignorePlayer(RunsafePlayer player, RunsafePlayer ignorePlayer)
	{
		database.ignorePlayer(player, ignorePlayer);
	}

	/**
	 * Removes a player from a players ignore list.
	 *
	 * @param player The player to perform the removal.
	 * @param ignorePlayer The player to be removed.
	 */
	public void removeIgnorePlayer(RunsafePlayer player, RunsafePlayer ignorePlayer)
	{
		database.removeIgnorePlayer(player, ignorePlayer);
	}

	/**
	 * Checks to see if a player is ignoring another player.
	 *
	 * @param player The player to check for.
	 * @param ignorePlayer The player who is being ignored.
	 * @return True if the player is being ignored.
	 */
	public boolean playerIsIgnoring(RunsafePlayer player, RunsafePlayer ignorePlayer)
	{
		String ignorePlayerName = ignorePlayer.getName();
		return ignoreList.containsKey(ignorePlayerName) && ignoreList.get(ignorePlayerName).contains(player.getName());
	}

	private final IgnoreDatabase database;
	private HashMap<String, List<String>> ignoreList;
}
