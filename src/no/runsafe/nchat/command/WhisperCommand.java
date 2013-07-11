package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafeAmbiguousPlayer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.handlers.MuteHandler;
import no.runsafe.nchat.handlers.WhisperHandler;

import java.util.HashMap;

public class WhisperCommand extends ExecutableCommand
{
	public WhisperCommand(WhisperHandler whisperHandler, MuteHandler muteHandler)
	{
		super("whisper", "Send a private message to another player", "runsafe.nchat.whisper", "player", "message");
		this.whisperHandler = whisperHandler;
		this.muteHandler = muteHandler;
		captureTail();
	}

	@Override
	public String OnExecute(ICommandExecutor executor, HashMap<String, String> parameters)
	{
		String targetPlayerName = parameters.get("player");
		RunsafePlayer target = RunsafeServer.Instance.getPlayer(targetPlayerName);

		if (target == null)
			return String.format(Constants.WHISPER_NO_TARGET, targetPlayerName);

		if (target instanceof RunsafeAmbiguousPlayer)
			return target.toString();

		if (executor instanceof RunsafePlayer)
		{
			RunsafePlayer playerExecutor = (RunsafePlayer) executor;
			if (this.whisperHandler.blockWhisper(playerExecutor, target))
				return String.format(Constants.WHISPER_TARGET_OFFLINE, targetPlayerName);

			if (this.muteHandler.isPlayerMuted(playerExecutor))
				return Constants.CHAT_MUTED;
		}

		this.whisperHandler.sendWhisper(executor, target, parameters.get("message"));
		return null;
	}

	private final WhisperHandler whisperHandler;
	private final MuteHandler muteHandler;
}
