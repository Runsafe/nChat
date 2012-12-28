package no.runsafe.nchat;

import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import org.apache.commons.lang.ArrayUtils;

import java.util.Collection;

public class ChannelCommand extends RunsafeCommand
{
    public ChannelCommand(ChatChannelHandler chatChannelHandler, Globals globals)
    {
        super("channel", null);
        this.chatChannelHandler = chatChannelHandler;
        this.globals = globals;
    }

    @Override
    public boolean Execute(RunsafePlayer player, String[] args)
    {
        if (args.length > 1)
        {
            String channelName = args[0].trim().toLowerCase();

            String[] messages = args;
            messages[0] = "";

            String message = this.globals.joinStringArray(messages);

            if (this.chatChannelHandler.channelExists(channelName))
                if (this.chatChannelHandler.canTalkInChannel(channelName, player))
                    this.chatChannelHandler.broadcastMessage(channelName, message, player);
                else
                    player.sendMessage(Constants.DEFAULT_MESSAGE_COLOR + Constants.CHANNEL_NO_PERMISSION);
            else
                player.sendMessage(Constants.DEFAULT_MESSAGE_COLOR + Constants.CHANNEL_NOT_EXIST);

            return true;
        }
        return false;
    }

    private ChatChannelHandler chatChannelHandler;
    private Globals globals;
}
