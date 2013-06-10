package no.runsafe.nchat.database;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import no.runsafe.framework.database.IDatabase;
import no.runsafe.framework.database.ISchemaChanges;
import no.runsafe.framework.database.Value;
import no.runsafe.framework.output.IOutput;

import javax.annotation.Nullable;
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
		return Lists.transform(
			database.QueryColumn("SELECT player FROM nchat_muted"),
			new Function<Value, String>()
			{
				@Override
				public String apply(@Nullable Value o)
				{
					return o.String();
				}
			}
		);
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
