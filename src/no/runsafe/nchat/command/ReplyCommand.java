package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.nchat.chat.MuteHandler;
import no.runsafe.nchat.chat.WhisperHandler;

import java.util.Map;

public class ReplyCommand extends PlayerCommand
{
	public ReplyCommand(WhisperHandler whisperHandler, MuteHandler muteHandler)
	{
		super("reply", "Respond to the last person to send you a private message", "runsafe.nchat.whisper", new TrailingArgument("message"));
		this.whisperHandler = whisperHandler;
		this.muteHandler = muteHandler;
	}

	@Override
	public String OnExecute(IPlayer player, Map<String, String> args)
	{
		IPlayer whisperer = this.whisperHandler.getLastWhisperedBy(player);

		if (whisperer == null)
		{
			if (this.whisperHandler.wasWhisperedByServer(player))
				this.whisperHandler.sendWhisperToConsole(player, args.get("message"));
			else
				return "&cYou have nothing to reply to.";
		}

		if (this.whisperHandler.blockWhisper(player, whisperer))
			return String.format("&cThe player %s is currently offline.", whisperer.getPrettyName());

		if (this.muteHandler.isPlayerMuted(player))
			return "&cYou are currently muted.";

		this.whisperHandler.sendWhisper(player, whisperer, args.get("message"));
		return null;
	}

	private final WhisperHandler whisperHandler;
	private final MuteHandler muteHandler;
}
