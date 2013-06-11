package no.runsafe.nchat.command;

import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.nchat.handlers.ChatHandler;

import java.util.HashMap;

public class EmoteCommand extends PlayerCommand
{
	public EmoteCommand(ChatHandler chatHandler)
	{
		super("me", "Broadcast an emote to the server", "runsafe.nchat.emote", "action");
		this.chatHandler = chatHandler;
		captureTail();
	}

	@Override
	public String OnExecute(RunsafePlayer player, HashMap<String, String> args)
	{
		RunsafeServer.Instance.broadcastMessage(this.chatHandler.formatPlayerSystemMessage(args.get("action"), player));
		return null;
	}

	private final ChatHandler chatHandler;

}
