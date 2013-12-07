package no.runsafe.nchat.chat;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.database.IgnoreDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public void ignorePlayer(IPlayer player, IPlayer ignorePlayer)
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
	public void removeIgnorePlayer(IPlayer player, IPlayer ignorePlayer)
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
	public boolean playerIsIgnoring(IPlayer player, IPlayer ignorePlayer)
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
	public boolean canIgnore(IPlayer ignorePlayer)
	{
		return !ignorePlayer.hasPermission("runsafe.nchat.ignore.exempt");
	}

	/**
	 * Returns a list of players who are ignoring a player.
	 *
	 * @param ignorePlayer The player to query for.
	 * @return A list of player names.
	 */
	public List<String> getPlayersIgnoring(IPlayer ignorePlayer)
	{
		String ignoreName = ignorePlayer.getName();
		if (ignoreList.containsKey(ignoreName))
			return ignoreList.get(ignoreName);

		return new ArrayList<String>(0);
	}

	/**
	 * Returns a list of players being ignored by a player.
	 *
	 * @param player The player who's list to return.
	 * @return A list of players being ignored.
	 */
	public List<String> getIgnoredPlayers(IPlayer player)
	{
		List<String> ignoredPlayers = new ArrayList<String>();

		for (Map.Entry<String, List<String>> node : ignoreList.entrySet())
			if (node.getValue().contains(player.getName()))
				ignoredPlayers.add(node.getKey());

		return ignoredPlayers;
	}

	private final IgnoreDatabase database;
	private HashMap<String, List<String>> ignoreList;
}
