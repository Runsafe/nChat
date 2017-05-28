package no.runsafe.nchat.database;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.database.*;
import no.runsafe.framework.api.player.IPlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class IgnoreDatabase extends Repository
{
	public IgnoreDatabase(IServer server)
	{
		this.server = server;
	}

	@Nonnull
	@Override
	public String getTableName()
	{
		return "nchat_ignore";
	}

	@Nonnull
	@Override
	public ISchemaUpdate getSchemaUpdateQueries()
	{
		ISchemaUpdate update = new SchemaUpdate();

		update.addQueries(
			"CREATE TABLE `nchat_ignore` (" +
				"`player` VARCHAR(16) NULL," +
				"`ignore` VARCHAR(16) NULL," +
				"PRIMARY KEY (`player`, `ignore`)" +
			')'
		);
		update.addQueries("DELETE FROM `nchat_ignore` WHERE player like `ignore`");
		return update;
	}

	public HashMap<IPlayer, List<IPlayer>> getIgnoreList()
	{
		HashMap<IPlayer, List<IPlayer>> ignoreList = new LinkedHashMap<>(1);
		ISet result = database.query("SELECT `player`, `ignore` FROM nchat_ignore");
		for (IRow row : result)
		{
			IPlayer ignoredPlayer = server.getPlayer(row.String("ignore"));
			if (!ignoreList.containsKey(ignoredPlayer))
				ignoreList.put(ignoredPlayer, new ArrayList<>(1));

			ignoreList.get(ignoredPlayer).add(server.getPlayer(row.String("player")));
		}

		return ignoreList;
	}

	public void ignorePlayer(IPlayer player, IPlayer ignore)
	{
		database.update("INSERT IGNORE INTO nchat_ignore (`player`, `ignore`) VALUES(?, ?)", player.getName(), ignore.getName());
	}

	public void removeIgnorePlayer(IPlayer player, IPlayer ignore)
	{
		database.update("DELETE FROM nchat_ignore WHERE `player` = ? AND `ignore` = ?", player.getName(), ignore.getName());
	}

	private final IServer server;
}
