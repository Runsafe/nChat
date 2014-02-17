package no.runsafe.nchat.database;

import no.runsafe.framework.api.database.IDatabase;
import no.runsafe.framework.api.database.ISchemaUpdate;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.api.database.SchemaUpdate;
import no.runsafe.framework.api.log.IDebug;

import java.util.*;

public class MuteDatabase extends Repository
{
	public MuteDatabase(IDebug console)
	{
		debugger = console;
	}

	@Override
	public String getTableName()
	{
		return "nchat_muted";
	}

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

		return update;
	}

	public Collection<String> getMuteList()
	{
		return database.queryStrings("SELECT player FROM nchat_muted");
	}

	public void mutePlayer(String playerName)
	{
		debugger.debugFine("Updating mute database with " + playerName);
		database.update("INSERT IGNORE INTO nchat_muted (`player`) VALUES (?)", playerName);
	}

	public void unMutePlayer(String playerName)
	{
		debugger.debugFine("Updating mute database with removal of " + playerName);
		database.execute("DELETE FROM nchat_muted WHERE player = ?", playerName);
	}

	private final IDebug debugger;
}
