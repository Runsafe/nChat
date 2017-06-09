package no.runsafe.nchat.database;

import no.runsafe.framework.api.database.*;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.api.server.IPlayerProvider;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class IgnoreDatabase extends Repository
{
	public IgnoreDatabase(IPlayerProvider playerProvider)
	{
		this.playerProvider = playerProvider;
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

		update.addQueries(
			String.format("ALTER TABLE `%s` MODIFY COLUMN `player` VARCHAR(36), MODIFY COLUMN `ignore` VARCHAR(36)", getTableName()),
			String.format( // Ignored player names -> Unique IDs
				"UPDATE IGNORE `%s` SET `ignore` = " +
					"COALESCE((SELECT `uuid` FROM player_db WHERE `name`=`%s`.`ignore`), `ignore`) " +
					"WHERE length(`ignore`) != 36",
				getTableName(), getTableName()
			),
			String.format( // Ignoring player names -> Unique IDs
				"UPDATE IGNORE `%s` SET `player` = " +
					"COALESCE((SELECT `uuid` FROM player_db WHERE `name`=`%s`.`player`), `player`) " +
					"WHERE length(`player`) != 36",
				getTableName(), getTableName()
			)
		);

		return update;
	}

	public HashMap<IPlayer, List<IPlayer>> getIgnoreList()
	{
		HashMap<IPlayer, List<IPlayer>> ignoreList = new LinkedHashMap<>(1);
		ISet result = database.query("SELECT `player`, `ignore` FROM nchat_ignore");
		for (IRow row : result)
		{
			IPlayer ignoredPlayer = playerProvider.getPlayer(UUID.fromString(row.String("ignore")));
			if (!ignoreList.containsKey(ignoredPlayer))
				ignoreList.put(ignoredPlayer, new ArrayList<>(1));

			ignoreList.get(ignoredPlayer).add(playerProvider.getPlayer(UUID.fromString(row.String("player"))));
		}

		return ignoreList;
	}

	public void ignorePlayer(IPlayer player, IPlayer ignore)
	{
		database.update("INSERT IGNORE INTO nchat_ignore (`player`, `ignore`) VALUES(?, ?)", player, ignore);
	}

	public void removeIgnorePlayer(IPlayer player, IPlayer ignore)
	{
		database.update("DELETE FROM nchat_ignore WHERE `player` = ? AND `ignore` = ?", player, ignore);
	}

	private final IPlayerProvider playerProvider;
}
