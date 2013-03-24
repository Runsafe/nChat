package no.runsafe.nchat.events;

import no.runsafe.framework.event.player.IPlayerChatEvent;
import no.runsafe.framework.output.ChatColour;
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
				event.setFormat(ChatColour.ToMinecraft(chatMessage.replaceAll(Constants.FORMAT_CHANNEL, "").trim()));
				this.spamHandler.checkForSpam(thePlayer, event.getMessage());
				return;
			}
		}
		else
		{
			thePlayer.sendColouredMessage(Constants.CHAT_MUTED);
		}
		event.setCancelled(true);
	}

	private final ChatHandler chatHandler;
	private final MuteHandler muteHandler;
	private final SpamHandler spamHandler;
}
