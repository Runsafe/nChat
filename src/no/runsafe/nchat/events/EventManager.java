package no.runsafe.nchat.events;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.player.IPlayerChatEvent;
import no.runsafe.framework.server.event.player.RunsafePlayerChatEvent;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.Globals;

public class EventManager implements IPlayerChatEvent, IConfigurationChanged
{
    public EventManager(ChatHandler chatHandler)
    {
        this.chatHandler = chatHandler;
    }

    @Override
    public void OnPlayerChatEvent(RunsafePlayerChatEvent event)
    {
        RunsafePlayer thePlayer = event.getPlayer();

        if (!this.chatMuted || thePlayer.hasPermission("nChat.muteExempt"))
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
    private boolean chatMuted;
}
