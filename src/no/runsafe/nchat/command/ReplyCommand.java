package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.WhisperHandler;

import javax.annotation.Nullable;

public class ReplyCommand extends PlayerCommand
{
	public ReplyCommand(WhisperHandler whisperHandler)
	{
		super("reply", "Respond to the last person to send you a private message", "runsafe.nchat.whisper", new TrailingArgument("message"));
		this.whisperHandler = whisperHandler;
	}

	@Nullable
	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		ICommandExecutor whisperer = whisperHandler.getLastWhisperedBy(executor);
		if (whisperer == null)
			return "&cYou have nothing to reply to.";

		whisperHandler.sendWhisper(executor, whisperer, parameters.get("message"));
		return null;
	}

	private final WhisperHandler whisperHandler;
}
