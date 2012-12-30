package no.runsafe.nchat;

import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.configuration.IConfigurationFile;
import no.runsafe.nchat.command.*;
import no.runsafe.nchat.database.MuteDatabase;
import no.runsafe.nchat.events.*;
import no.runsafe.nchat.handlers.ChatChannelHandler;
import no.runsafe.nchat.handlers.ChatHandler;
import no.runsafe.nchat.handlers.MuteHandler;
import no.runsafe.nchat.handlers.WhisperHandler;

import java.io.InputStream;

public class Core extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		// TODO: Implement anti-spam feature
		// TODO: Implement an AFK system

		// Core
		this.addComponent(Globals.class);
		this.addComponent(DeathParser.class);

		// Database
		this.addComponent(MuteDatabase.class);

		// Handlers
		this.addComponent(ChatChannelHandler.class);
		this.addComponent(ChatHandler.class);
		this.addComponent(MuteHandler.class);
		this.addComponent(WhisperHandler.class);

		// Commands
		//this.addComponent(ChannelCommand.class);
		this.addComponent(MuteCommand.class);
		this.addComponent(UnMuteCommand.class);
		this.addComponent(PlayerDeath.class);
		this.addComponent(PuppetCommand.class);
		this.addComponent(EmoteCommand.class);
		this.addComponent(WhisperCommand.class);
		this.addComponent(ReplyCommand.class);

		// Events
		this.addComponent(JoinEvent.class);
		this.addComponent(LeaveEvent.class);
		this.addComponent(EventManager.class);
		this.addComponent(KickEvent.class);
	}
}
