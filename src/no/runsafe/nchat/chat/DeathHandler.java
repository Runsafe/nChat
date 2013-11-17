package no.runsafe.nchat.chat;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerDeathEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.files.PluginDataFile;
import no.runsafe.framework.files.PluginFileManager;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerDeathEvent;

import java.util.List;
import java.util.Random;

public class DeathHandler implements IPlayerDeathEvent, IConfigurationChanged
{
	public DeathHandler(PluginFileManager fileManager)
	{
		deathMessageFile = fileManager.getFile("death_messages.txt");
	}

	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		messages = deathMessageFile.getLines();
	}

	@Override
	public void OnPlayerDeathEvent(RunsafePlayerDeathEvent event)
	{
		if (!messages.isEmpty()) // We have no messages!
		{
			event.setDeathMessage(""); // Set the Minecraft death message to blank to silence output
			String message = messages.get(random.nextInt(messages.size())); // Get a random death message.
			RunsafeServer.Instance.broadcastMessage(message.replaceAll("#player", event.getEntity().getPrettyName()));
		}
	}

	private PluginDataFile deathMessageFile;
	private List<String> messages;
	private Random random = new Random();
}
