package no.runsafe.nchat.command;

import no.runsafe.framework.command.player.PlayerCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;

import java.util.HashMap;

public class PlayerListRefreshCommand extends PlayerCommand
{
	public PlayerListRefreshCommand(ChatHandler chatHandler)
	{
		super("tabrefresh", "Refresh a players name as displayed in the tab list", "runsafe.nchat.playerlistrefresh", "player");
		this.chatHandler = chatHandler;
	}

	@Override
	public String OnExecute(RunsafePlayer player, HashMap<String, String> args)
	{
		RunsafePlayer refreshPlayer = RunsafeServer.Instance.getPlayer(args.get("player"));
		if (refreshPlayer == null)
			return String.format("The player %s does not exist.", args.get("player"));
		this.chatHandler.refreshPlayerTabListName(refreshPlayer);
		return String.format(
			"The tab list name for %s has been updated",
			refreshPlayer.getPrettyName()
		);
	}

	private final ChatHandler chatHandler;
}
