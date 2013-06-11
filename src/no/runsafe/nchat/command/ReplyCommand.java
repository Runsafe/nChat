package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.handlers.MuteHandler;
import no.runsafe.nchat.handlers.WhisperHandler;

import java.util.HashMap;

public class ReplyCommand extends PlayerCommand
{
	public ReplyCommand(WhisperHandler whisperHandler, MuteHandler muteHandler)
	{
		super("reply", "Respond to the last person to send you a private message", "runsafe.nchat.whisper", "message");
		this.whisperHandler = whisperHandler;
		this.muteHandler = muteHandler;
		captureTail();
	}

	@Override
	public String OnExecute(RunsafePlayer player, HashMap<String, String> args)
	{
		RunsafePlayer whisperer = this.whisperHandler.getLastWhisperedBy(player);

		if (whisperer == null)
			return Constants.WHISPER_NO_REPLY_TARGET;

		if (!this.whisperHandler.canWhisper(player, whisperer))
			return String.format(Constants.WHISPER_TARGET_OFFLINE, whisperer.getName());

		if (this.muteHandler.isPlayerMuted(player))
			return Constants.CHAT_MUTED;

		this.whisperHandler.sendWhisper(player, whisperer, args.get("message"));
		return null;
	}

	private final WhisperHandler whisperHandler;
	private final MuteHandler muteHandler;
}
