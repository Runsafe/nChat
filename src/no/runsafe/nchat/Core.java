package no.runsafe.nchat;

import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.configuration.IConfigurationFile;
import no.runsafe.nchat.command.ChannelCommand;
import no.runsafe.nchat.command.MuteCommand;
import no.runsafe.nchat.command.PuppetCommand;
import no.runsafe.nchat.command.UnMuteCommand;
import no.runsafe.nchat.database.MuteDatabase;
import no.runsafe.nchat.events.EventManager;
import no.runsafe.nchat.events.JoinEvent;
import no.runsafe.nchat.events.LeaveEvent;
import no.runsafe.nchat.events.PlayerDeath;
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
		// TODO: Implement emote command
		// TODO: Implement custom kick/ban messages
		// TODO: Implement fake quit/join

		// Core
		this.addComponent(Globals.class);

		// Database
		this.addComponent(MuteDatabase.class);

		// Handlers
		this.addComponent(ChatChannelHandler.class);
		this.addComponent(ChatHandler.class);
		this.addComponent(MuteHandler.class);

		// Commands
		this.addComponent(ChannelCommand.class);
		this.addComponent(MuteCommand.class);
		this.addComponent(UnMuteCommand.class);
		this.addComponent(PlayerDeath.class);
		this.addComponent(PuppetCommand.class);

		// Events
		this.addComponent(JoinEvent.class);
		this.addComponent(LeaveEvent.class);
		this.addComponent(EventManager.class);
	}
}
