package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.argument.TrailingArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;

import java.util.HashMap;
import java.util.Map;

public class EmoteCommand extends PlayerCommand
{
	public EmoteCommand(ChatHandler chatHandler)
	{
		super("me", "An emote!", "runsafe.nchat.emote", new TrailingArgument("action"));
		this.chatHandler = chatHandler;
	}

	@Override
	public String OnExecute(RunsafePlayer player, Map<String, String> args)
	{
		RunsafeServer.Instance.broadcastMessage(this.chatHandler.formatPlayerSystemMessage(args.get("action"), player));
		return null;
	}

	private final ChatHandler chatHandler;
	private HashMap<String, String> emotes = new HashMap<String, String>();

	static
	{

	}
}
