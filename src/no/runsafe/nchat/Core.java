package no.runsafe.nchat;

import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.nchat.antispam.*;
import no.runsafe.nchat.chat.*;
import no.runsafe.nchat.chat.formatting.ChatFormatter;
import no.runsafe.nchat.chat.formatting.FormattingConfig;
import no.runsafe.nchat.chat.formatting.RegionHandler;
import no.runsafe.nchat.command.*;
import no.runsafe.nchat.database.IgnoreDatabase;
import no.runsafe.nchat.database.MuteDatabase;
import no.runsafe.nchat.emotes.EmoteHandler;
import no.runsafe.nchat.events.*;
import no.runsafe.nchat.tablist.TabListHandler;
import no.runsafe.worldguardbridge.WorldGuardInterface;

public class Core extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		addComponent(Instances.get("RunsafeWorldGuardBridge").getComponent(WorldGuardInterface.class));

		// Core
		addComponent(Utils.class);

		// Database
		addComponent(MuteDatabase.class);
		addComponent(IgnoreDatabase.class);

		// Anti-spam
		addComponent(DuplicationFilter.class);
		addComponent(IPFilter.class);
		addComponent(FloodFilter.class);
		addComponent(BlacklistFilter.class);
		addComponent(CapsFilter.class);
		addComponent(SpamHandler.class);

		// Chat engine
		addComponent(FormattingConfig.class);
		addComponent(ChatFormatter.class);
		addComponent(IgnoreHandler.class);
		addComponent(ChatEngine.class);
		addComponent(MuteHandler.class);
		addComponent(WhisperHandler.class);
		addComponent(RegionHandler.class);
		addComponent(TabListHandler.class);
		addComponent(EmoteHandler.class);

		// Commands
		addComponent(MuteCommand.class);
		addComponent(UnMuteCommand.class);
		addComponent(PuppetCommand.class);
		addComponent(WhisperCommand.class);
		addComponent(ReplyCommand.class);
		addComponent(DeathHandler.class);
		addComponent(IgnoreCommand.class);

		// Events
		addComponent(JoinEvent.class);
		addComponent(LeaveEvent.class);
		addComponent(KickEvent.class);
		addComponent(ChatEvent.class);
		addComponent(VanishEvent.class);
	}
}
