package no.runsafe.nchat.database;

import no.runsafe.framework.api.database.IRow;
import no.runsafe.framework.api.database.ISchemaUpdate;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.api.database.SchemaUpdate;
import no.runsafe.framework.api.log.IDebug;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.api.server.IPlayerProvider;
import org.joda.time.DateTime;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.Instant;

public class MuteDatabase extends Repository
{
	public MuteDatabase(IDebug console, IPlayerProvider playerProvider)
	{
		debugger = console;
		this.playerProvider = playerProvider;
	}

	@Nonnull
	@Override
	public String getTableName()
	{
		return "nchat_muted";
	}

	@Nonnull
	@Override
	public ISchemaUpdate getSchemaUpdateQueries()
	{
		ISchemaUpdate update = new SchemaUpdate();

		update.addQueries(
			"CREATE TABLE `nchat_muted` (" +
				"`player` VARCHAR(255) NULL," +
				"PRIMARY KEY (`player`)" +
			')'
		);
		update.addQueries("ALTER TABLE `nchat_muted` ADD COLUMN temp_mute datetime NULL");
		update.addQueries("ALTER TABLE `nchat_muted` ADD COLUMN `shadow` TINYINT(1) NOT NULL DEFAULT '0' AFTER `temp_mute`");

		update.addQueries(
			String.format("ALTER TABLE `%s` DROP COLUMN `shadow`", getTableName()),
			String.format( // Muted player names -> Unique IDs
				"UPDATE IGNORE `%s` SET `player` = " +
					"COALESCE((SELECT `uuid` FROM player_db WHERE `name`=`%s`.`player`), `player`) " +
					"WHERE length(`player`) != 36",
				getTableName(), getTableName()
			)
		);

		return update;
	}

	public Map<IPlayer, Instant> getMuteList()
	{
		Map<IPlayer, Instant> mutes = new HashMap<>(0);
		for (IRow row : database.query("SELECT player, temp_mute FROM nchat_muted"))
		{
			DateTime expiryJoda = row.DateTime("temp_mute");
			Instant expiry = null;
			if (expiryJoda != null)
				expiry = Instant.ofEpochMilli(expiryJoda.getMillis());

			String player = row.String("player");
			if (player != null && player.length() == 36)
				mutes.put(playerProvider.getPlayer(UUID.fromString(player)), expiry == null ? END_OF_TIME : expiry);
		}
		return mutes;
	}

	public void mutePlayer(IPlayer player)
	{
		debugger.debugFine("Updating mute database with " + player.getName());
		database.update("INSERT IGNORE INTO nchat_muted (`player`) VALUES (?)", player);
	}

	public void tempMutePlayer(IPlayer player, Instant expire)
	{
		database.update(
			"INSERT IGNORE INTO nchat_muted (`player`,`temp_mute`) VALUES (?, ?)",
			player, new DateTime(expire.toEpochMilli())
		);
	}

	public void unMutePlayer(IPlayer player)
	{
		debugger.debugFine("Updating mute database with removal of " + player.getName());
		database.execute("DELETE FROM nchat_muted WHERE player = ?", player);
	}

	private final IDebug debugger;
	private final IPlayerProvider playerProvider;
	public static final Instant END_OF_TIME = Instant.ofEpochMilli(Long.MAX_VALUE);
}
