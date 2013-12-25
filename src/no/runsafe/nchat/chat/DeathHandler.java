package no.runsafe.nchat.chat;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.event.player.IPlayerDeathEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.filesystem.IPluginDataFile;
import no.runsafe.framework.api.filesystem.IPluginFileManager;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerDeathEvent;

import java.util.List;
import java.util.Random;

public class DeathHandler implements IPlayerDeathEvent, IConfigurationChanged
{
	public DeathHandler(IPluginFileManager fileManager, PlayerChatEngine chatEngine)
	{
		this.chatEngine = chatEngine;
		deathMessageFile = fileManager.getFile("death_messages.txt");
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		messages = deathMessageFile.getLines();
		ignoreWorlds = configuration.getConfigValueAsList("hideDeaths");
	}

	@Override
	public void OnPlayerDeathEvent(RunsafePlayerDeathEvent event)
	{
		event.setDeathMessage(""); // Set the Minecraft death message to blank to silence output
		if (!messages.isEmpty() && canBroadcastHere(event.getEntity().getWorld())) // We have no messages!
		{
			String message = messages.get(random.nextInt(messages.size())); // Get a random death message.
			chatEngine.broadcastMessage(message.replaceAll("#player", event.getEntity().getPrettyName()));
		}
	}

	private boolean canBroadcastHere(IWorld world)
	{
		return world != null && !ignoreWorlds.contains(world.getName());
	}

	private final IPluginDataFile deathMessageFile;
	private List<String> messages;
	private List<String> ignoreWorlds;
	private final Random random = new Random();
	private final PlayerChatEngine chatEngine;
}
