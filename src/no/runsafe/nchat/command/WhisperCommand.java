package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.Player;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.WhisperHandler;

import javax.annotation.Nullable;

public class WhisperCommand extends ExecutableCommand
{
	public WhisperCommand(WhisperHandler whisperHandler)
	{
		super("whisper", "Send a private message to another player", "runsafe.nchat.whisper", new Player().onlineOnly().require(), new TrailingArgument("message").require());
		this.whisperHandler = whisperHandler;
	}

	@Nullable
	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		whisperHandler.sendWhisper(executor, (IPlayer) parameters.getValue("player"), (String) parameters.getRequired("message"));
		return null;
	}

	private final WhisperHandler whisperHandler;
}
