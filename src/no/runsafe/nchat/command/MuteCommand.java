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
        super("mute", "player");
		this.console = console;
		this.muteHandler = muteHandler;
    }

	@Override
	public String requiredPermission()
	{
		return "runsafe.nchat.mute";
	}

	@Override
	public String OnExecute(RunsafePlayer executor, String[] args)
	{
		String mutePlayerName = args[0].trim();

		if (mutePlayerName.equalsIgnoreCase("server"))
		{
			if (executor.hasPermission("runsafe.nchat.mute.server"))
			{
				this.muteHandler.muteServer();
				executor.sendMessage(Constants.DEFAULT_MESSAGE_COLOR + Constants.COMMAND_CHAT_MUTED);
				console.write(String.format("%s muted server chat.", executor.getName()));
			}
			else
			{
				executor.sendMessage(Constants.COMMAND_NO_PERMISSION);
			}
		}
		else
		{
			RunsafePlayer mutePlayer = RunsafeServer.Instance.getPlayer(mutePlayerName);

			if (mutePlayer != null)
			{
				if (!mutePlayer.hasPermission("runsafe.nchat.mute.exempt"))
				{
					console.write(String.format(
							"%s muted %s",
							executor.getName(),
							mutePlayer.getName()
					));
					this.muteHandler.mutePlayer(mutePlayer);
				}
				else
				{
					executor.sendMessage(Constants.COMMAND_TARGET_EXEMPT);
				}
			}
			else
			{
				executor.sendMessage(Constants.COMMAND_TARGET_NO_EXISTS);
			}
		}

		return null;
	}

	private final IOutput console;
	private final MuteHandler muteHandler;
}
