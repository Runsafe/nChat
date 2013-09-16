package no.runsafe.nchat.emotes;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.event.player.IPlayerCommandPreprocessEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerCommandPreprocessEvent;
import no.runsafe.framework.minecraft.player.RunsafeAmbiguousPlayer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmoteHandler implements IPlayerCommandPreprocessEvent, IConfigurationChanged
{
	public EmoteHandler(IOutput output)
	{
		this.output = output;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.emotes.clear();
		String path = "plugins/nChat/";
		File pathDir = new File(path);
		if (!pathDir.exists())
		{
			if (!pathDir.mkdirs())
			{
				output.logError("Could not create directory tree for nChat emotes!");
				return;
			}
		}

		File file = new File(path + "emotes.txt");
		if (!file.exists())
		{
			try
			{
				if (!file.createNewFile())
				{
					output.logError("Could not create emotes file.");
					return;
				}

				BufferedReader br = new BufferedReader(new FileReader(file));
				String currentLine;

				while ((currentLine = br.readLine()) != null)
					emotes.add(new Emote(currentLine));
				br.close();

				this.output.logInformation("Loaded %s emotes!", emotes.size());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
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

	private IOutput output;
	private List<Emote> emotes = new ArrayList<Emote>();
}
