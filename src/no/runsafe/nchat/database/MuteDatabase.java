package no.runsafe.nchat.database;

import no.runsafe.framework.api.database.IDatabase;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.api.log.IDebug;

import java.util.*;

public class MuteDatabase extends Repository
{
	public MuteDatabase(IDebug console, IDatabase database)
	{
		debugger = console;
		this.database = database;
	}

	@Override
	public String getTableName()
	{
		return "nchat_muted";
	}

	@Override
	public HashMap<Integer, List<String>> getSchemaUpdateQueries()
	{
		HashMap<Integer, List<String>> queries = new LinkedHashMap<Integer, List<String>>(1);
		List<String> sql = new ArrayList<String>(1);
		sql.add(
			"CREATE TABLE `nchat_muted` (" +
				"`player` VARCHAR(255) NULL," +
				"PRIMARY KEY (`player`)" +
				')'
		);
		queries.put(1, sql);
		return queries;
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
	private final IDatabase database;
}
