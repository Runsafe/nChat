package no.runsafe.nchat.emotes;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerCommandPreprocessEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.files.PluginDataFile;
import no.runsafe.framework.files.PluginFileManager;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerCommandPreprocessEvent;
import no.runsafe.framework.minecraft.player.RunsafeAmbiguousPlayer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.chat.ChatEngine;

import java.util.ArrayList;
import java.util.List;

public class EmoteHandler implements IPlayerCommandPreprocessEvent, IConfigurationChanged
{
	public EmoteHandler(PluginFileManager fileManager, ChatEngine chatEngine)
	{
		this.chatEngine = chatEngine;
		emoteFile = fileManager.getFile("emotes.txt");
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.emotes.clear(); // Clear existing emotes.

		List<String> emotes = emoteFile.getLines(); // Grab all emotes from the file.
		for (String emote : emotes)
			this.emotes.add(new Emote(emote)); // Add the emote to the list.
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
				event.cancel();
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
			chatEngine.playerSystemBroadcast(player, String.format(emote.getTargetEmote(), player.getPrettyName(), target.getPrettyName()));
		else
			chatEngine.playerSystemBroadcast(player, String.format(emote.getSingleEmote(), player.getPrettyName()));
	}

	private final ChatEngine chatEngine;
	private PluginDataFile emoteFile;
	private List<Emote> emotes = new ArrayList<Emote>();
}
