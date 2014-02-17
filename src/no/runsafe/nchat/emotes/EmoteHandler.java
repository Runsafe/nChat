package no.runsafe.nchat.emotes;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.event.player.IPlayerCommandPreprocessEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.filesystem.IPluginDataFile;
import no.runsafe.framework.api.filesystem.IPluginFileManager;
import no.runsafe.framework.api.player.IAmbiguousPlayer;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerCommandPreprocessEvent;
import no.runsafe.nchat.chat.PlayerChatEngine;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmoteHandler implements IPlayerCommandPreprocessEvent, IConfigurationChanged
{
	public EmoteHandler(IPluginFileManager fileManager, PlayerChatEngine chatEngine, IServer server)
	{
		this.chatEngine = chatEngine;
		this.server = server;
		emoteFile = fileManager.getFile("emotes.txt");
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		emotes.clear(); // Clear existing emotes.

		List<String> definitions = emoteFile.getLines(); // Grab all emotes from the file.
		for (String definition : definitions)
		{
			EmoteDefinition emote = new EmoteDefinition(definition);
			emotes.put(emote.getEmote(), emote); // Add the emote to the list.
		}
		emoteChecker = Pattern.compile("^/(" + StringUtils.join(emotes.keySet(), '|') + ")( (\\S+)|)");
	}

	@Override
	public void OnBeforePlayerCommand(RunsafePlayerCommandPreprocessEvent event)
	{
		if (event.isCancelled())
			return;

		if (executeEmote(event.getPlayer(), event.getMessage()))
			event.cancel();
	}

	public boolean executeEmote(IPlayer player, CharSequence command)
	{
		Matcher matcher = emoteChecker.matcher(command);
		if (matcher.matches())
		{
			EmoteDefinition emote = emotes.get(matcher.group(1));
			IPlayer targetPlayer = matcher.groupCount() > 2 ? server.getPlayer(matcher.group(3)) : null;
			broadcastEmote(emote, player, targetPlayer);
			return true;
		}
		return false;
	}

	private void broadcastEmote(EmoteDefinition emote, IPlayer player, IPlayer target)
	{
		if (target instanceof IAmbiguousPlayer)
		{
			player.sendColouredMessage(target.toString());
			return;
		}

		if (target != null)
			chatEngine.playerSystemBroadcast(player, String.format(emote.getTargetEmote(), player.getPrettyName(), target.getPrettyName()));
		else
			chatEngine.playerSystemBroadcast(player, String.format(emote.getSingleEmote(), player.getPrettyName()));
	}

	private final PlayerChatEngine chatEngine;
	private final IServer server;
	private final IPluginDataFile emoteFile;
	private final Map<String, EmoteDefinition> emotes = new HashMap<String, EmoteDefinition>(0);
	private Pattern emoteChecker;
}
