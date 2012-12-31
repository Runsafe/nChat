package no.runsafe.nchat.command;

import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.handlers.MuteHandler;
import no.runsafe.nchat.handlers.WhisperHandler;
import org.apache.commons.lang.StringUtils;

public class WhisperCommand extends RunsafeCommand
{
	public WhisperCommand(WhisperHandler whisperHandler, MuteHandler muteHandler)
	{
		super("whisper", "player", "message");
		this.whisperHandler = whisperHandler;
		this.muteHandler = muteHandler;
	}

	@Override
	public String OnExecute(RunsafePlayer executor, String[] args)
	{
		RunsafePlayer targetPlayer = RunsafeServer.Instance.getPlayer(args[0]);

		if (targetPlayer != null)
		{
			if (this.whisperHandler.canWhisper(executor, targetPlayer))
			{
				if (!this.muteHandler.isPlayerMuted(executor))
				{
					String message = StringUtils.join(args, " ", 1, args.length);
					this.whisperHandler.sendWhisper(executor, targetPlayer, message);
				}
				else
				{
					executor.sendMessage(Constants.CHAT_MUTED);
				}
			}
			else
			{
				executor.sendMessage(String.format(Constants.WHISPER_TARGET_OFFLINE, targetPlayer.getName()));
			}
		}
		else
		{
			executor.sendMessage(String.format(Constants.WHISPER_NO_TARGET, args[0]));
		}
		return null;
	}

	@Override
	public String requiredPermission()
	{
		return "runsafe.nchat.whisper";
	}

	private WhisperHandler whisperHandler;
	private MuteHandler muteHandler;
}
