package no.runsafe.nchat.chat;

import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.database.IgnoreDatabase;

import java.util.ArrayList;
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
		String ignorePlayerName = ignorePlayer.getName();

		if (!ignoreList.containsKey(ignorePlayerName))
			ignoreList.put(ignorePlayerName, new ArrayList<String>());

		ignoreList.get(ignorePlayerName).add(player.getName());
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
		String ignorePlayerName = ignorePlayer.getName();

		if (ignoreList.containsKey(ignorePlayerName))
		{
			List<String> playersIgnoring = ignoreList.get(ignorePlayerName);
			playersIgnoring.remove(player.getName());

			// Check if we still have anyone ignoring the player, no need to keep empty lists.
			if (playersIgnoring.size() == 0)
				ignoreList.remove(ignorePlayerName);
		}
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

	/**
	 * Checks to see if a player can be ignored.
	 *
	 * @param ignorePlayer The player to check.
	 * @return True if the player can be ignored, otherwise false.
	 */
	public boolean canIgnore(RunsafePlayer ignorePlayer)
	{
		return !ignorePlayer.hasPermission("runsafe.nchat.ignore.exempt");
	}

	/**
	 * Returns a list of players who are ignoring a player.
	 *
	 * @param ignorePlayer The player to query for.
	 * @return A list of player names.
	 */
	public List<String> getPlayersIgnoring(RunsafePlayer ignorePlayer)
	{
		String ignoreName = ignorePlayer.getName();
		if (ignoreList.containsKey(ignoreName))
			return ignoreList.get(ignoreName);

		return new ArrayList<String>(0);
	}

	private final IgnoreDatabase database;
	private HashMap<String, List<String>> ignoreList;
}
