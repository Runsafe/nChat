package no.runsafe.nchat;

import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.server.player.RunsafePlayer;

public class MuteCommand extends RunsafeCommand
{

    public MuteCommand(IConfiguration configuration)
    {
        super("mute", null);
        this.configuration = configuration;
    }

    @Override
    public boolean Execute(RunsafePlayer player, String[] args)
    {
        if (args.length == 1)
        {
            String mutePlayerName = args[0].trim();

            if (!mutePlayerName.isEmpty())
            {
                if (mutePlayerName == "server")
                {
                    if (player.hasPermission("nChat.commands.muteServer"))
                    {
                        if (this.configuration.getConfigValueAsBoolean("nChat.spamControl.muteChat"))
                        {
                            this.configuration.setConfigValue("spamControl.muteChat", false);
                            player.sendMessage(Constants.DEFAULT_MESSAGE_COLOR + Constants.COMMAND_CHAT_MUTED);
                        }
                        else
                        {
                            this.configuration.setConfigValue("spamControl.muteChat", true);
                            player.sendMessage(Constants.DEFAULT_MESSAGE_COLOR + Constants.COMMAND_CHAT_UNMUTED);
                        }
                    }
                }
                else
                {
                    if (player.hasPermission("nChat.commands.mutePlayer"))
                    {
                        RunsafePlayer mutePlayer = new RunsafePlayer(mutePlayerName);
                        //mute player
                    }
                }
            }
        }
        return false;
    }

    private IConfiguration configuration;
}
