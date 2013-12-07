package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.PlayerArgument;
import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.player.IAmbiguousPlayer;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.chat.MuteHandler;
import no.runsafe.nchat.chat.WhisperHandler;

import java.util.Map;

public class WhisperCommand extends ExecutableCommand
{
	public WhisperCommand(WhisperHandler whisperHandler, MuteHandler muteHandler)
	{
		super("whisper", "Send a private message to another player", "runsafe.nchat.whisper", new PlayerArgument(), new TrailingArgument("message"));
		this.whisperHandler = whisperHandler;
		this.muteHandler = muteHandler;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, Map<String, String> parameters)
	{
		String targetPlayerName = parameters.get("player");
		IPlayer target = RunsafeServer.Instance.getPlayer(targetPlayerName);

		if (target == null)
			return String.format("&c%s does not exist.", targetPlayerName);

		if (target instanceof IAmbiguousPlayer)
			return target.toString();

		if (executor instanceof RunsafePlayer)
		{
			RunsafePlayer playerExecutor = (RunsafePlayer) executor;
			if (this.whisperHandler.blockWhisper(playerExecutor, target))
				return String.format("&cThe player %s is offline.", targetPlayerName);

			if (this.muteHandler.isPlayerMuted(playerExecutor))
				return "&cYou are currently muted!";
		}

		this.whisperHandler.sendWhisper(executor, target, parameters.get("message"));
		return null;
	}

	private final WhisperHandler whisperHandler;
	private final MuteHandler muteHandler;
}
