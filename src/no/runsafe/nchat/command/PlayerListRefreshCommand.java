package no.runsafe.nchat.command;

import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;

public class PlayerListRefreshCommand extends RunsafeCommand
{
	public PlayerListRefreshCommand(ChatHandler chatHandler)
	{
		super("tabrefresh", "player");
		this.chatHandler = chatHandler;
	}

	@Override
	public String OnExecute(RunsafePlayer executor, String[] args)
	{
		RunsafePlayer refreshPlayer = RunsafeServer.Instance.getPlayer(args[0]);

		if (refreshPlayer != null)
		{
			this.chatHandler.refreshPlayerTabListName(refreshPlayer);
			executor.sendMessage(String.format(
					"The tab list name for %s has been updated",
					refreshPlayer.getPrettyName()
			));
		}
		else
			executor.sendMessage(String.format("The player %s does not exist.", args[0]));

		return null;
	}

	@Override
	public String requiredPermission()
	{
		return "runsafe.nchat.playerlistrefresh";
	}

	private final ChatHandler chatHandler;
}
