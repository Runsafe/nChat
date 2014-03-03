package no.runsafe.nchat.command;

import com.sun.org.apache.regexp.internal.recompile;
import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.Player;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.MuteHandler;
import no.runsafe.nchat.chat.WhisperHandler;

import javax.annotation.Nullable;

public class WhisperCommand extends ExecutableCommand
{
	public WhisperCommand(WhisperHandler whisperHandler, MuteHandler muteHandler)
	{
		super("whisper", "Send a private message to another player", "runsafe.nchat.whisper", new Player.Online().require(), new TrailingArgument("message"));
		this.whisperHandler = whisperHandler;
		this.muteHandler = muteHandler;
	}

	@Nullable
	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		String targetPlayerName = parameters.get("player");
		IPlayer target = parameters.getValue("player");

		if (target == null)
			return String.format("&c%s does not exist.", targetPlayerName);

		if (executor instanceof IPlayer)
		{
			IPlayer playerExecutor = (IPlayer) executor;
			if (WhisperHandler.blockWhisper(playerExecutor, target))
				return String.format("&cThe player %s is offline.", targetPlayerName);

			if (muteHandler.isPlayerMuted(playerExecutor))
				return "&cYou are currently muted!";
		}

		whisperHandler.sendWhisper(executor, target, parameters.get("message"));
		return null;
	}

	private final WhisperHandler whisperHandler;
	private final MuteHandler muteHandler;
}
