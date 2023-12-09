package no.runsafe.nchat.chat;

import no.runsafe.framework.api.event.IServerReady;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.database.IgnoreDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IgnoreHandler implements IServerReady
{
	public IgnoreHandler(IgnoreDatabase database)
	{
		this.database = database;
		ignoreList = new HashMap<>(0);
	}

	@Override
	public void OnServerReady()
	{
		ignoreList.putAll(database.getIgnoreList());
	}

	/**
	 * Adds a player to the players ignore list.
	 *
	 * @param player       The player who will ignore.
	 * @param ignorePlayer The player to be ignored.
	 */
	public void ignorePlayer(IPlayer player, IPlayer ignorePlayer)
	{
		database.ignorePlayer(player, ignorePlayer);

		if (!ignoreList.containsKey(ignorePlayer))
			ignoreList.put(ignorePlayer, new ArrayList<>(1));

		ignoreList.get(ignorePlayer).add(player);
	}

	/**
	 * Removes a player from a players ignore list.
	 *
	 * @param player       The player to perform the removal.
	 * @param ignorePlayer The player to be removed.
	 */
	public void removeIgnorePlayer(IPlayer player, IPlayer ignorePlayer)
	{
		database.removeIgnorePlayer(player, ignorePlayer);

		if (!ignoreList.containsKey(ignorePlayer))
			return;

		List<IPlayer> playersIgnoring = ignoreList.get(ignorePlayer);
		playersIgnoring.remove(player);

		// Check if we still have anyone ignoring the player, no need to keep empty lists.
		if (playersIgnoring.isEmpty())
			ignoreList.remove(ignorePlayer);
	}

	/**
	 * Checks to see if a player is ignoring another player.
	 *
	 * @param player       The player to check for.
	 * @param ignorePlayer The player who is being ignored.
	 * @return True if the player is being ignored.
	 */
	public boolean playerIsIgnoring(IPlayer player, IPlayer ignorePlayer)
	{
		return ignoreList.containsKey(ignorePlayer) && ignoreList.get(ignorePlayer).contains(player);
	}

	/**
	 * Checks to see if either player is ignoring eachother.
	 *
	 * @param playerOne The first player argument.
	 * @param playerTwo The second player argument.
	 * @return True if either player is ignoring the other.
	 */
	public boolean eitherPlayerIsIgnoring (IPlayer playerOne, IPlayer playerTwo)
	{
		return playerIsIgnoring(playerOne, playerTwo) || playerIsIgnoring(playerTwo, playerOne);
	}

	/**
	 * Returns a list of players who are ignoring a player.
	 *
	 * @param ignorePlayer The player to query for.
	 * @return A list of player names.
	 */
	public List<IPlayer> getPlayersIgnoring(IPlayer ignorePlayer)
	{
		if (ignoreList.containsKey(ignorePlayer))
			return ignoreList.get(ignorePlayer);

		return new ArrayList<>(0);
	}

	/**
	 * Returns a list of players being ignored by a player.
	 *
	 * @param player The player who's list to return.
	 * @return A list of players being ignored.
	 */
	public List<IPlayer> getIgnoredPlayers(IPlayer player)
	{
		List<IPlayer> ignoredPlayers = new ArrayList<>(0);

		for (Map.Entry<IPlayer, List<IPlayer>> node : ignoreList.entrySet())
			if (node.getValue().contains(player))
				ignoredPlayers.add(node.getKey());

		return ignoredPlayers;
	}

	private final IgnoreDatabase database;
	private final HashMap<IPlayer, List<IPlayer>> ignoreList;
}
