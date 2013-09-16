package no.runsafe.nchat.emotes;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.event.player.IPlayerCommandPreprocessEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerCommandPreprocessEvent;
import no.runsafe.framework.minecraft.player.RunsafeAmbiguousPlayer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.Core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmoteHandler implements IPlayerCommandPreprocessEvent, IConfigurationChanged
{
	public EmoteHandler(IOutput output, Core nChat)
	{
		this.output = output;
		dataFile = new File(nChat.getDataFolder(), "emotes.txt");
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.emotes.clear();

		// Check if the file exists.
		if (!dataFile.exists())
		{
			// The file does not exist, lets try creating it.
			try
			{
				if (!dataFile.getParentFile().isDirectory())
				{
					this.output.warning("Unable to locate plugin data folder at: " + dataFile.getParentFile());
					return;
				}
				if (!dataFile.createNewFile())
				{
					this.output.warning("Unable to create emote file at: " + dataFile);
					return;
				}
			}
			catch (IOException exception)
			{
				this.output.warning("Unable to create emote file due to exception:");
				exception.printStackTrace();
				return;
			}
		}

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			String line;
			while (true)
			{
				line = reader.readLine();
				if (line == null)
					break;

				emotes.add(new Emote(line));
			}
			output.logInformation("Loaded %s emotes from file.", emotes.size());
		}
		catch (Exception exception)
		{
			// We should not get here, but we catch it just in-case.
			this.output.warning("Unexpected exception prevented emote loading:");
			exception.printStackTrace();
		}
	}

	@Override
	public void OnBeforePlayerCommand(RunsafePlayerCommandPreprocessEvent event)
	{
		if (event.isCancelled())
			return;

		String[] parts = event.getMessage().split(" ");
		for (Emote emote : emotes)
		{
			if (parts[0].equalsIgnoreCase("/" + emote.getEmote()))
			{
				RunsafePlayer targetPlayer = parts.length > 1 ? RunsafeServer.Instance.getPlayer(parts[1]) : null;
				this.broadcastEmote(emote, event.getPlayer(),  targetPlayer);
				break;
			}
		}

		event.cancel();
	}

	private void broadcastEmote(Emote emote, RunsafePlayer player, RunsafePlayer target)
	{
		if (target instanceof RunsafeAmbiguousPlayer)
		{
			player.sendColouredMessage(target.toString());
			return;
		}

		if (target != null)
		{
			RunsafeServer.Instance.broadcastMessage(String.format(
					emote.getTargetEmote(), player.getPrettyName(), target.getPrettyName()
			));
		}
		else
		{
			RunsafeServer.Instance.broadcastMessage(String.format(
					emote.getSingleEmote(), player.getPrettyName()
			));
		}
	}

	private File dataFile;
	private IOutput output;
	private List<Emote> emotes = new ArrayList<Emote>();
}
