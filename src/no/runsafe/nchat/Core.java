package no.runsafe.nchat;

import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.configuration.IConfigurationFile;
import no.runsafe.nchat.command.ChannelCommand;
import no.runsafe.nchat.command.MuteCommand;
import no.runsafe.nchat.command.UnMuteCommand;
import no.runsafe.nchat.events.EventManager;
import no.runsafe.nchat.handlers.ChatChannelHandler;
import no.runsafe.nchat.handlers.ChatHandler;
import no.runsafe.nchat.handlers.MuteHandler;

import java.io.InputStream;

public class Core extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		// TODO: Implement anti-spam feature
		// TODO: Implement Server join/leave messages
		// TODO: Implement Death messages

		this.addComponent(Globals.class);
		this.addComponent(ChatChannelHandler.class);
		this.addComponent(ChatHandler.class);
		this.addComponent(EventManager.class);
		this.addComponent(MuteHandler.class);
		this.addComponent(ChannelCommand.class);
		this.addComponent(MuteCommand.class);
		this.addComponent(UnMuteCommand.class);
	}
}
