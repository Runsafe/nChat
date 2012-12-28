package no.runsafe.nchat.database;

import no.runsafe.framework.database.IDatabase;
import no.runsafe.framework.database.ISchemaChanges;
import no.runsafe.framework.output.IOutput;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MuteDatabase implements ISchemaChanges
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
		console.fine("Populating mute list from database");
		PreparedStatement fetch = database.prepare(
				"SELECT player FROM nchat_muted"
		);

		try
		{
			ResultSet data = fetch.executeQuery();
			ArrayList<String> result = new ArrayList<String>();
			while (data.next())
				result.add(data.getString("player"));
			return result;
		}
		catch (SQLException e)
		{
			console.write(e.getMessage());
			return null;
		}
	}

	public void mutePlayer(String playerName)
	{
		console.fine("Updating mute database with " + playerName);
		PreparedStatement update = database.prepare(
			"INSERT IGNORE INTO nchat_muted (`playerName`) VALUES (?)"
		);

		try
		{
			update.setString(1, playerName);
			update.executeUpdate();
		}
		catch (SQLException e)
		{
			console.write(e.getMessage());
		}
	}

	public void unMutePlayer(String playerName)
	{
		console.fine("Updating mute database with removal of " + playerName);
		PreparedStatement update = database.prepare(
			"DELETE FROM nchat_muted WHERE playerName = ?"
		);

		try
		{
			update.setString(1, playerName);
			update.executeUpdate();
		}
		catch (SQLException e)
		{
			console.write(e.getMessage());
		}
	}

	private IOutput console;
	private IDatabase database;
}
