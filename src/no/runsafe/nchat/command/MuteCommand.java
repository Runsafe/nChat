package no.runsafe.nchat.command;

import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.handlers.MuteHandler;

public class MuteCommand extends RunsafeCommand
{

    public MuteCommand(IOutput console, MuteHandler muteHandler)
    {
        super("mute");
		this.console = console;
		this.muteHandler = muteHandler;
    }

	// TODO: Use OnExecute. Docpify smells.
    @Override
    public boolean Execute(RunsafePlayer player, String[] args)
    {
        if (args.length == 1)
        {
            String mutePlayerName = args[0].trim();

            if (!mutePlayerName.isEmpty())
            {
                if (mutePlayerName.equalsIgnoreCase("server"))
                {
                    if (player.hasPermission("nChat.commands.muteServer"))
                    {
						this.muteHandler.muteServer();
						player.sendMessage(Constants.DEFAULT_MESSAGE_COLOR + Constants.COMMAND_CHAT_MUTED);
						console.write(String.format("%s muted server chat.", player.getName()));
                    }
					else
					{
						player.sendMessage(Constants.COMMAND_NO_PERMISSION);
					}
                }
                else
                {
                    if (player.hasPermission("nChat.commands.mutePlayer"))
                    {
                        RunsafePlayer mutePlayer = RunsafeServer.Instance.getPlayer(mutePlayerName);

						if (mutePlayer != null)
						{
							if (!mutePlayer.hasPermission("nChat.muteExempt"))
							{
								console.write(String.format(
										"%s muted %s",
										player.getName(),
										mutePlayer.getName()
								));
								this.muteHandler.mutePlayer(mutePlayer);
							}
							else
							{
								player.sendMessage(Constants.COMMAND_TARGET_EXEMPT);
							}
						}
						else
						{
							player.sendMessage(Constants.COMMAND_TARGET_NO_EXISTS);
						}
					}
					else
					{
						player.sendMessage(Constants.COMMAND_NO_PERMISSION);
					}
                }
            }
			else
			{
				player.sendMessage(Constants.COMMAND_ENTER_PLAYER);
			}

			return true;
        }
        return false;
    }

	private IOutput console;
	private MuteHandler muteHandler;
}
