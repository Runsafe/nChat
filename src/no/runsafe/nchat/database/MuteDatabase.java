package no.runsafe.nchat.database;

import no.runsafe.framework.api.database.IRow;
import no.runsafe.framework.api.database.ISchemaUpdate;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.api.database.SchemaUpdate;
import no.runsafe.framework.api.log.IDebug;
import org.joda.time.DateTime;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class MuteDatabase extends Repository
{
	public MuteDatabase(IDebug console)
	{
		debugger = console;
	}

	@Override
	@Nonnull
	public String getTableName()
	{
		return "nchat_muted";
	}

	@Override
	@Nonnull
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
		return update;
	}

	public Map<String, DateTime> getMuteList()
	{
		Map<String, DateTime> mutes = new HashMap<String, DateTime>(0);
		for (IRow row : database.query("SELECT player, temp_mute FROM nchat_muted"))
		{
			DateTime expiry = row.DateTime("temp_mute");
			if (row.String("player") != null)
				mutes.put(row.String("player"), expiry == null ? END_OF_TIME : expiry);
		}
		return mutes;
	}

	public void mutePlayer(String playerName)
	{
		debugger.debugFine("Updating mute database with " + playerName);
		database.update("INSERT IGNORE INTO nchat_muted (`player`) VALUES (?)", playerName);
	}

	public void tempMutePlayer(String playerName, DateTime expire)
	{
		database.update("INSERT IGNORE INTO nchat_muted (`player`,`temp_mute`) VALUES (?, ?)", playerName, expire);
	}

	public void unMutePlayer(String playerName)
	{
		debugger.debugFine("Updating mute database with removal of " + playerName);
		database.execute("DELETE FROM nchat_muted WHERE player = ?", playerName);
	}

	private final IDebug debugger;
	public static final DateTime END_OF_TIME = new DateTime(Long.MAX_VALUE);
}
