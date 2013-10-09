package no.runsafe.nchat;

import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.nchat.antispam.*;
import no.runsafe.nchat.command.*;
import no.runsafe.nchat.database.MuteDatabase;
import no.runsafe.nchat.emotes.EmoteHandler;
import no.runsafe.nchat.events.*;
import no.runsafe.nchat.handlers.*;
import no.runsafe.worldguardbridge.WorldGuardInterface;

public class Core extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		// TODO: Re-implement channel functionality
		// TODO: In-game whisper monitoring. Do we want this?

		addComponent(Instances.get("RunsafeWorldGuardBridge").getComponent(WorldGuardInterface.class));

		// Core
		this.addComponent(Globals.class);

		// Database
		this.addComponent(MuteDatabase.class);

		// Anti-spam
		this.addComponent(DuplicationFilter.class);
		this.addComponent(IPFilter.class);
		this.addComponent(FloodFilter.class);
		this.addComponent(BlacklistFilter.class);
		this.addComponent(CapsFilter.class);
		this.addComponent(SpamHandler.class);

		// Handlers
		this.addComponent(ChatChannelHandler.class);
		this.addComponent(ChatHandler.class);
		this.addComponent(MuteHandler.class);
		this.addComponent(WhisperHandler.class);
		this.addComponent(RegionHandler.class);

		// Commands
		//this.addComponent(ChannelCommand.class);
		this.addComponent(MuteCommand.class);
		this.addComponent(UnMuteCommand.class);
		this.addComponent(PuppetCommand.class);
		//this.addComponent(EmoteCommand.class);
		this.addComponent(WhisperCommand.class);
		this.addComponent(ReplyCommand.class);
		this.addComponent(DeathHandler.class);

		// Emotes
		this.addComponent(EmoteHandler.class);

		// Events
		this.addComponent(JoinEvent.class);
		this.addComponent(LeaveEvent.class);
		this.addComponent(KickEvent.class);
		this.addComponent(ChatEvent.class);
		this.addComponent(VanishEvent.class);
	}
}
