package no.runsafe.nchat.database;

import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.database.IRow;
import no.runsafe.framework.api.database.ISchemaUpdate;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.api.database.SchemaUpdate;
import no.runsafe.framework.api.log.IDebug;
import no.runsafe.framework.api.player.IPlayer;
import org.joda.time.DateTime;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class MuteDatabase extends Repository
{
	public MuteDatabase(IDebug console, IServer server)
	{
		debugger = console;
		this.server = server;
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

		update.addQueries(String.format("ALTER TABLE `%s` DROP COLUMN `shadow`", getTableName()));

		return update;
	}

	public Map<IPlayer, DateTime> getMuteList()
	{
		Map<IPlayer, DateTime> mutes = new HashMap<>(0);
		for (IRow row : database.query("SELECT player, temp_mute FROM nchat_muted"))
		{
			DateTime expiry = row.DateTime("temp_mute");
			if (row.String("player") != null)
				mutes.put(server.getPlayer(row.String("player")), expiry == null ? END_OF_TIME : expiry);
		}
		return mutes;
	}

	public void mutePlayer(IPlayer player)
	{
		debugger.debugFine("Updating mute database with " + player.getName());
		database.update("INSERT IGNORE INTO nchat_muted (`player`) VALUES (?)", player.getName());
	}

	public void tempMutePlayer(IPlayer player, DateTime expire)
	{
		database.update("INSERT IGNORE INTO nchat_muted (`player`,`temp_mute`) VALUES (?, ?)", player.getName(), expire);
	}

	public void unMutePlayer(IPlayer player)
	{
		debugger.debugFine("Updating mute database with removal of " + player.getName());
		database.execute("DELETE FROM nchat_muted WHERE player = ?", player.getName());
	}

	private final IDebug debugger;
	private final IServer server;
	public static final DateTime END_OF_TIME = new DateTime(Long.MAX_VALUE);
}
