package no.runsafe.nchat;

import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.features.Commands;
import no.runsafe.framework.features.Database;
import no.runsafe.framework.features.Events;
import no.runsafe.framework.features.FrameworkHooks;
import no.runsafe.nchat.channel.ChannelManager;
import no.runsafe.nchat.channel.ChannelSelector;
import no.runsafe.nchat.channel.GlobalChatChannel;
import no.runsafe.nchat.channel.IChannelManager;
import no.runsafe.nchat.filter.*;
import no.runsafe.nchat.chat.*;
import no.runsafe.nchat.chat.formatting.ChatFormatter;
import no.runsafe.nchat.chat.formatting.MentionHighlighter;
import no.runsafe.nchat.chat.formatting.RegionHandler;
import no.runsafe.nchat.command.*;
import no.runsafe.nchat.database.IgnoreDatabase;
import no.runsafe.nchat.database.MuteDatabase;
import no.runsafe.nchat.emotes.EmoteHandler;
import no.runsafe.nchat.events.*;
import no.runsafe.nchat.tablist.PlayerTablistNameHandler;

public class ChatPlugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void pluginSetup()
	{
		// Framework features used
		addComponent(Commands.class);
		addComponent(Events.class);
		addComponent(Database.class);
		addComponent(FrameworkHooks.class);

		// Database
		addComponent(MuteDatabase.class);
		addComponent(IgnoreDatabase.class);

		// Filters
		addComponent(DuplicationFilter.class);
		addComponent(IPFilter.class);
		addComponent(FloodFilter.class);
		addComponent(BlacklistFilter.class);
		addComponent(CapsFilter.class);
		addComponent(SpamHandler.class);
		addComponent(MuteFilter.class);
		addComponent(IgnoreFilter.class);

		// Chat engine
		addComponent(MentionHighlighter.class);
		addComponent(ChatFormatter.class);
		addComponent(IgnoreHandler.class);
		addComponent(MuteHandler.class);
		addComponent(WhisperHandler.class);
		addComponent(RegionHandler.class);
		addComponent(PlayerTablistNameHandler.class);
		addComponent(EmoteHandler.class);

		// Channels
		addComponent(ChannelManager.class);
		addComponent(GlobalChatChannel.class);
		addComponent(ChannelSelector.class);

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

		// External
		addComponent(PlayerDataProvider.class);
	}
}
