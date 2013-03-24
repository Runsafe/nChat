package no.runsafe.nchat.command;

import no.runsafe.framework.command.player.PlayerCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.handlers.MuteHandler;
import no.runsafe.nchat.handlers.WhisperHandler;

import java.util.HashMap;

public class WhisperCommand extends PlayerCommand
{
	public WhisperCommand(WhisperHandler whisperHandler, MuteHandler muteHandler)
	{
		super("whisper", "Send a private message to another player", "runsafe.nchat.whisper", "player", "message");
		this.whisperHandler = whisperHandler;
		this.muteHandler = muteHandler;
		captureTail();
	}

	@Override
	public String OnExecute(RunsafePlayer player, HashMap<String, String> args)
	{
		RunsafePlayer targetPlayer = RunsafeServer.Instance.getPlayer(args.get("player"));

		if (targetPlayer != null)
		{
			if (this.whisperHandler.canWhisper(player, targetPlayer))
			{
				if (!this.muteHandler.isPlayerMuted(player))
					this.whisperHandler.sendWhisper(player, targetPlayer, args.get("message"));
				else
					player.sendMessage(Constants.CHAT_MUTED);
			}
			else
			{
				player.sendMessage(String.format(Constants.WHISPER_TARGET_OFFLINE, targetPlayer.getName()));
			}
		}
		else
		{
			player.sendMessage(String.format(Constants.WHISPER_NO_TARGET, args.get("player")));
		}
		return null;
	}

	private final WhisperHandler whisperHandler;
	private final MuteHandler muteHandler;
}
