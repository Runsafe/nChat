package no.runsafe.nchat.channel;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.event.player.IPlayerJoinEvent;
import no.runsafe.framework.api.event.player.IPlayerQuitEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerJoinEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerQuitEvent;

public class GlobalChatChannel extends BasicChatChannel implements IPlayerJoinEvent, IPlayerQuitEvent, IConfigurationChanged
{
	public GlobalChatChannel(IChannelManager manager, IConsole console)
	{
		super(console, manager, CHANNEL_NAME);
		manager.registerChannel(this);
	}

	@Override
	public boolean Join(ICommandExecutor player)
	{
		return false;
	}

	@Override
	public boolean Leave(ICommandExecutor player)
	{
		return false;
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent event)
	{
		if (event.isFake())
		{
			SendSystem(joinServerMessage.replace("#player", event.getPlayer().getPrettyName()));
			return;
		}
		if (!super.Join(event.getPlayer()))
			throw new RuntimeException("Unable to join %s to global channel");
		manager.setDefaultChannel(event.getPlayer(), this);
		SendSystemFiltered(joinServerMessage.replace("#player", event.getPlayer().getPrettyName()), event.getPlayer());
	}

	@Override
	public void OnPlayerQuit(RunsafePlayerQuitEvent event)
	{
		if (event.isFake())
		{
			SendSystem(leaveServerMessage.replace("#player", event.getPlayer().getPrettyName()));
			return;
		}

		SendSystemFiltered(leaveServerMessage.replace("#player", event.getPlayer().getPrettyName()), event.getPlayer());

		manager.removeChannelFromList(event.getPlayer(), this);
		if (!super.Leave(event.getPlayer()))
			throw new RuntimeException("Unable to remove %s from global channel");
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		joinServerMessage = configuration.getConfigValueAsString("chatMessage.joinServer");
		leaveServerMessage = configuration.getConfigValueAsString("chatMessage.leaveServer");
	}

	private String joinServerMessage;
	private String leaveServerMessage;
	public static final String CHANNEL_NAME = "global";
}
