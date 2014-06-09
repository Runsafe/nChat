package no.runsafe.nchat.chat;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.entity.IEntity;
import no.runsafe.framework.api.event.player.IPlayerDeathEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.filesystem.IPluginDataFile;
import no.runsafe.framework.api.filesystem.IPluginFileManager;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerDeathEvent;
import no.runsafe.nchat.channel.GlobalChatChannel;
import no.runsafe.nchat.channel.IChannelManager;
import no.runsafe.nchat.channel.IChatChannel;

import java.util.List;
import java.util.Random;

public class DeathHandler implements IPlayerDeathEvent, IConfigurationChanged
{
	public DeathHandler(IPluginFileManager fileManager, IChannelManager manager)
	{
		deathMessageFile = fileManager.getFile("death_messages.txt");
		pvpDeathMessageFile = fileManager.getFile("pvp_death_messages.txt");
		channel = manager.getChannelByName(GlobalChatChannel.CHANNELNAME);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		messages = deathMessageFile.getLines();
		pvpMessages = pvpDeathMessageFile.getLines();
		ignoreWorlds = configuration.getConfigValueAsList("hideDeaths");
	}

	@Override
	public void OnPlayerDeathEvent(RunsafePlayerDeathEvent event)
	{
		event.setDeathMessage(""); // Set the Minecraft death message to blank to silence output
		if (!messages.isEmpty() && canBroadcastHere(event.getEntity().getWorld())) // We have no messages!
		{
			String message;
			IPlayer player = event.getEntity();

			IEntity killer = player.getKiller();
			if (killer instanceof IPlayer)
			{
				IPlayer killerPlayer = (IPlayer) killer;
				message = pvpMessages.get(random.nextInt(pvpMessages.size()));
				message = message.replaceAll("#killer", killerPlayer.getPrettyName());
			}
			else
			{
				message = messages.get(random.nextInt(messages.size()));
			}

			message = message.replaceAll("#player", player.getPrettyName());
			channel.SendSystem(message);
		}
	}

	private boolean canBroadcastHere(IWorld world)
	{
		return world != null && !ignoreWorlds.contains(world.getName());
	}

	private final IPluginDataFile deathMessageFile;
	private final IPluginDataFile pvpDeathMessageFile;
	private List<String> messages;
	private List<String> pvpMessages;
	private List<String> ignoreWorlds;
	private final Random random = new Random();
	private final IChatChannel channel;
}
