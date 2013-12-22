package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.MuteHandler;
import no.runsafe.nchat.chat.WhisperHandler;

import javax.annotation.Nullable;
import java.util.Map;

public class ReplyCommand extends PlayerCommand
{
	public ReplyCommand(WhisperHandler whisperHandler, MuteHandler muteHandler)
	{
		super("reply", "Respond to the last person to send you a private message", "runsafe.nchat.whisper", new TrailingArgument("message"));
		this.whisperHandler = whisperHandler;
		this.muteHandler = muteHandler;
	}

	@Nullable
	@Override
	public String OnExecute(IPlayer executor, Map<String, String> parameters)
	{
		IPlayer whisperer = whisperHandler.getLastWhisperedBy(executor);

		if (whisperer == null)
		{
			if (whisperHandler.wasWhisperedByServer(executor))
			{
				whisperHandler.sendWhisperToConsole(executor, parameters.get("message"));
				return null;
			}
			else
				return "&cYou have nothing to reply to.";
		}

		if (WhisperHandler.blockWhisper(executor, whisperer))
			return String.format("&cThe player %s is currently offline.", whisperer.getPrettyName());

		if (muteHandler.isPlayerMuted(executor))
			return "&cYou are currently muted.";

		whisperHandler.sendWhisper(executor, whisperer, parameters.get("message"));
		return null;
	}

	private final WhisperHandler whisperHandler;
	private final MuteHandler muteHandler;
}
