package no.runsafe.nchat.database;

import no.runsafe.framework.api.database.IRow;
import no.runsafe.framework.api.database.ISchemaUpdate;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.api.database.SchemaUpdate;
import no.runsafe.framework.api.log.IDebug;
import no.runsafe.nchat.chat.MuteEntry;
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
		update.addQueries("ALTER TABLE `nchat_muted` ADD COLUMN `shadow` TINYINT(1) NOT NULL DEFAULT '0' AFTER `temp_mute`");
		return update;
	}

	public Map<String, MuteEntry> getMuteList()
	{
		Map<String, MuteEntry> mutes = new HashMap<String, MuteEntry>(0);
		for (IRow row : database.query("SELECT player, temp_mute, shadow FROM nchat_muted"))
		{
			DateTime expiry = row.DateTime("temp_mute");
			if (row.String("player") != null)
			{
				String playerName = row.String("player");
				mutes.put(playerName, new MuteEntry(playerName, expiry == null ? END_OF_TIME : expiry, row.Integer("shadow") > 0));
			}
		}
		return mutes;
	}

	public void mutePlayer(String playerName, boolean shadow)
	{
		debugger.debugFine("Updating mute database with " + playerName);
		database.update("INSERT IGNORE INTO nchat_muted (`player`, `shadow`) VALUES (?, ?)", playerName, shadow ? 1 : 0);
	}

	public void tempMutePlayer(String playerName, DateTime expire, boolean shadow)
	{
		database.update("INSERT IGNORE INTO nchat_muted (`player`,`temp_mute`, `shadow`) VALUES (?, ?, ?)", playerName, expire, shadow ? 1 : 0);
	}

	public void unMutePlayer(String playerName)
	{
		debugger.debugFine("Updating mute database with removal of " + playerName);
		database.execute("DELETE FROM nchat_muted WHERE player = ?", playerName);
	}

	private final IDebug debugger;
	public static final DateTime END_OF_TIME = new DateTime(Long.MAX_VALUE);
}
