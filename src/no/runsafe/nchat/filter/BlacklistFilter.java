package no.runsafe.nchat.filter;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class BlacklistFilter implements ISpamFilter, IConfigurationChanged
{
	public BlacklistFilter(Plugin nChat, IConsole output)
	{
		this.output = output;
		filePath = new File(nChat.getDataFolder(), "blacklist.txt");
	}

	@Override
	@Nullable
	public String processString(IPlayer player, String message)
	{
		// Parse the message to lowercase to prevent bypassing that way.
		String lowerMessage = message.toLowerCase();

		// Check each node, if we find it in the string, cancel the message.
		for (String blacklisted : blacklist)
		{
			if (lowerMessage.contains(blacklisted))
			{
				player.sendColouredMessage("&cYour last message was blacklisted and not sent, sorry.");
				return null;
			}
		}

		return message;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		blacklist.clear(); // Clear the current blacklist.

		// Check if the file exists.
		if (!filePath.exists())
		{
			// The file does not exist, lets try creating it.
			try
			{
				if (filePath.getParentFile().isDirectory())
				{
					if (!filePath.createNewFile())
					{
						output.logWarning("Unable to create blacklist file at: %s", filePath);
						return;
					}
				}
				else
				{
					output.logWarning("Unable to locate plugin data folder at: %s", filePath.getParentFile());
					return;
				}
			}
			catch (IOException exception)
			{
				output.logWarning("Unable to create blacklist file due to exception:");
				output.logException(exception);
				return;
			}
		}

		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(filePath));
			while (true)
			{
				String line = reader.readLine();
				if (line == null)
					break;
				// Add every line we find to the blacklist array, converting it to lowercase.
				blacklist.add(line.toLowerCase());
			}
			output.logInformation("Loaded %d blacklist filters from file.", blacklist.size());
		}
		catch (Exception exception)
		{
			// We should not get here, but we catch it just in-case.
			output.logWarning("Unexpected exception prevented blacklist loading:");
			output.logException(exception);
		}
		finally
		{
			if (reader != null)
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					output.logException(e);
				}
		}
	}

	private final Collection<String> blacklist = new ArrayList<>(0);
	private final File filePath;
	private final IConsole output;
}
