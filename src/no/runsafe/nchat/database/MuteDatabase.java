package no.runsafe.nchat.database;

import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.database.IDatabase;
import no.runsafe.framework.api.database.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MuteDatabase extends Repository
{
	public MuteDatabase(IOutput console, IDatabase database)
	{
		this.console = console;
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
		HashMap<Integer, List<String>> queries = new HashMap<Integer, List<String>>();
		ArrayList<String> sql = new ArrayList<String>();
		sql.add(
			"CREATE TABLE `nchat_muted` (" +
				"`player` VARCHAR(255) NULL," +
				"PRIMARY KEY (`player`)" +
				")"
		);
		queries.put(1, sql);
		return queries;
	}

	public List<String> getMuteList()
	{
		return this.database.QueryStrings("SELECT player FROM nchat_muted");
	}

	public void mutePlayer(String playerName)
	{
		console.fine("Updating mute database with " + playerName);
		database.Update("INSERT IGNORE INTO nchat_muted (`player`) VALUES (?)", playerName);
	}

	public void unMutePlayer(String playerName)
	{
		console.fine("Updating mute database with removal of " + playerName);
		database.Execute("DELETE FROM nchat_muted WHERE player = ?", playerName);
	}

	private final IOutput console;
	private final IDatabase database;
}
