package no.runsafe.nchat.chat;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.text.ChatColour;
import no.runsafe.nchat.antispam.SpamHandler;

import java.util.HashMap;

public class WhisperHandler implements IConfigurationChanged
{
	public WhisperHandler(IServer server, IConsole console, SpamHandler spamHandler, IgnoreHandler ignoreHandler)
	{
		this.server = server;
		this.console = console;
		this.spamHandler = spamHandler;
		this.ignoreHandler = ignoreHandler;
		this.lastWhisperList = new HashMap<String, String>();
	}

	public void sendWhisper(ICommandExecutor sender, IPlayer toPlayer, String message)
	{
		if (sender instanceof IPlayer)
		{
			IPlayer senderPlayer = (IPlayer) sender;

			if (ignoreHandler.playerIsIgnoring(toPlayer, senderPlayer))
			{
				// The player being whispered is ignoring the sender.
				senderPlayer.sendColouredMessage("&cThat player is ignoring you.");
				return;
			}

			if (ignoreHandler.playerIsIgnoring(senderPlayer, toPlayer))
			{
				// The player is ignoring the person they are whispering.
				senderPlayer.sendColouredMessage("&cYou are ignoring that player.");
				return;
			}

			message = spamHandler.getFilteredMessage(senderPlayer, message);
		}

		if (message != null)
		{
			if (!(enableColorCodes || sender.hasPermission("runsafe.nchat.colors")))
				message = ChatColour.Strip(message);

			sender.sendColouredMessage(
				whisperToFormat.replace("#target", toPlayer.getPrettyName()).replace("#message", message)
			);

			String senderName = (sender instanceof IPlayer ? ((IPlayer) sender).getPrettyName() : consoleWhisperName);

			toPlayer.sendColouredMessage(
				whisperFromFormat.replace("#source", senderName).replace("#message", message)
			);

			if (sender instanceof IPlayer)
				setLastWhisperedBy(toPlayer, (IPlayer) sender);

			console.logInformation("%s -> %s: %s", senderName, toPlayer.getPrettyName(), message);
		}
	}

	private void setLastWhisperedBy(IPlayer player, IPlayer whisperer)
	{
		lastWhisperList.put(player.getName(), whisperer.getName());
	}

	public void deleteLastWhisperedBy(IPlayer player)
	{
		if (lastWhisperList.containsKey(player.getName()))
			lastWhisperList.remove(player.getName());
	}

	public IPlayer getLastWhisperedBy(IPlayer player)
	{
		String playerName = player.getName();

		if (lastWhisperList.containsKey(playerName))
		{
			IPlayer whisperer = server.getPlayer(lastWhisperList.get(playerName));
			if (whisperer != null)
				return whisperer;
		}
		return null;
	}

	public boolean blockWhisper(IPlayer player, IPlayer target)
	{
		return !target.isOnline() || player.shouldNotSee(target);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		whisperToFormat = configuration.getConfigValueAsString("chatMessage.whisperTo");
		whisperFromFormat = configuration.getConfigValueAsString("chatMessage.whisperFrom");
		enableColorCodes = configuration.getConfigValueAsBoolean("nChat.enableColorCodes");
		consoleWhisperName = configuration.getConfigValueAsString("console.whisper");
	}

	private boolean enableColorCodes;
	private String whisperToFormat;
	private String whisperFromFormat;
	private String consoleWhisperName;
	private final HashMap<String, String> lastWhisperList;
	private final IConsole console;
	private final SpamHandler spamHandler;
	private final IgnoreHandler ignoreHandler;
	private final IServer server;
}
