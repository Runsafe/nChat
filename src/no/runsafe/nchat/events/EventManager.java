package no.runsafe.nchat.events;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.player.IPlayerChatEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerChatEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.Globals;
import no.runsafe.nchat.handlers.MuteHandler;

public class EventManager implements IPlayerChatEvent, IConfigurationChanged
{
	// TODO: Move this into proper event class
    public EventManager(ChatHandler chatHandler, MuteHandler muteHandler)
    {
        this.chatHandler = chatHandler;
		this.muteHandler = muteHandler;
    }

    @Override
    public void OnPlayerChatEvent(RunsafePlayerChatEvent event)
    {
        RunsafePlayer thePlayer = event.getPlayer();
		boolean isMuted = (this.chatMuted || this.muteHandler.isPlayerMuted(thePlayer));

        if (!isMuted || thePlayer.hasPermission("nChat.muteExempt"))
        {
            String chatMessage = this.chatHandler.formatChatMessage(event.getMessage(), thePlayer);

            if (chatMessage != null)
            {
                event.setFormat(chatMessage.replaceAll(Constants.FORMAT_CHANNEL, "").trim());
                return;
            }
        }
        else
        {
            thePlayer.sendMessage(Constants.DEFAULT_MESSAGE_COLOR + Constants.CHAT_MUTED);
        }
        event.setCancelled(true);
    }

    @Override
    public void OnConfigurationChanged(IConfiguration iConfiguration)
    {
        this.chatMuted = iConfiguration.getConfigValueAsBoolean("spamControl.muteChat");
    }

    private ChatHandler chatHandler;
	private MuteHandler muteHandler;
    private boolean chatMuted;
}
