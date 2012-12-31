package no.runsafe.nchat.events;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.player.IPlayerChatEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerChatEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.handlers.ChatHandler;
import no.runsafe.nchat.handlers.MuteHandler;
import no.runsafe.nchat.handlers.SpamHandler;

public class ChatEvent implements IPlayerChatEvent
{
	public ChatEvent(ChatHandler chatHandler, MuteHandler muteHandler, SpamHandler spamHandler)
	{
		this.chatHandler = chatHandler;
		this.muteHandler = muteHandler;
		this.spamHandler = spamHandler;
	}

	@Override
	public void OnPlayerChatEvent(RunsafePlayerChatEvent event)
	{
		RunsafePlayer thePlayer = event.getPlayer();

		if (!this.muteHandler.isPlayerMuted(thePlayer))
		{
			String chatMessage = this.chatHandler.formatChatMessage(event.getMessage(), thePlayer);

			if (chatMessage != null)
			{
				event.setFormat(chatMessage.replaceAll(Constants.FORMAT_CHANNEL, "").trim());
				this.spamHandler.checkForSpam(thePlayer, event.getMessage());
				return;
			}
		}
		else
		{
			thePlayer.sendMessage(Constants.CHAT_MUTED);
		}
		event.setCancelled(true);
	}

	private ChatHandler chatHandler;
	private MuteHandler muteHandler;
	private SpamHandler spamHandler;
}
