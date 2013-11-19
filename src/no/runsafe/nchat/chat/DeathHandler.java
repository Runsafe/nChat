package no.runsafe.nchat.chat;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.player.IPlayerDeathEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.files.PluginDataFile;
import no.runsafe.framework.files.PluginFileManager;
import no.runsafe.framework.minecraft.RunsafeWorld;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerDeathEvent;

import java.util.List;
import java.util.Random;

public class DeathHandler implements IPlayerDeathEvent, IConfigurationChanged
{
	public DeathHandler(PluginFileManager fileManager, ChatEngine chatEngine)
	{
		this.chatEngine = chatEngine;
		deathMessageFile = fileManager.getFile("death_messages.txt");
	}

	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		messages = deathMessageFile.getLines();
		ignoreWorlds = config.getConfigValueAsList("hideDeaths");
	}

	@Override
	public void OnPlayerDeathEvent(RunsafePlayerDeathEvent event)
	{
		if (!messages.isEmpty() && canBroadcastHere(event.getEntity().getWorld())) // We have no messages!
		{
			event.setDeathMessage(""); // Set the Minecraft death message to blank to silence output
			String message = messages.get(random.nextInt(messages.size())); // Get a random death message.
			chatEngine.broadcastMessage(message.replaceAll("#player", event.getEntity().getPrettyName()));
		}
	}

	private boolean canBroadcastHere(RunsafeWorld world)
	{
		return world != null && !ignoreWorlds.contains(world.getName());
	}

	private PluginDataFile deathMessageFile;
	private List<String> messages;
	private List<String> ignoreWorlds;
	private Random random = new Random();
	private final ChatEngine chatEngine;
}
