package no.runsafe.nchat.chat;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.api.player.IPlayerVisibility;
import no.runsafe.framework.text.ChatColour;
import no.runsafe.nchat.channel.ChannelManager;
import no.runsafe.nchat.channel.IChatChannel;
import no.runsafe.nchat.filter.SpamHandler;

import javax.annotation.Nullable;
import java.util.HashMap;

public class WhisperHandler implements IConfigurationChanged
{
	private static final String SERVER = "@_SERVER!\0";

	public WhisperHandler(IServer server, IConsole console, SpamHandler spamHandler, IgnoreHandler ignoreHandler, ChannelManager manager)
	{
		this.server = server;
		this.console = console;
		this.spamHandler = spamHandler;
		this.ignoreHandler = ignoreHandler;
		this.manager = manager;
		lastWhisperList = new HashMap<String, String>(0);
	}

	public void sendWhisper(ICommandExecutor sender, IPlayer target, String message)
	{
		InternalChatEvent event = new InternalChatEvent(sender instanceof IPlayer ? (IPlayer) sender : null, message);
		IChatChannel channel = manager.getPrivateChannel(sender, target);
		manager.setDefaultChannel(sender, channel);
		channel.Send(event);
		setLastWhisperedBy(target, sender);
	}

	public void sendWhisperToConsole(IPlayer sender, String message)
	{
		if (!(enableColorCodes || sender.hasPermission("runsafe.nchat.colors")))
			message = ChatColour.Strip(message);

		sender.sendColouredMessage(
			whisperToFormat.replace("#target", consoleWhisperName).replace("#message", message)
		);

		console.logInformation("%s -> %s: %s", sender.getPrettyName(), consoleWhisperName, message);

	}

	private void setLastWhisperedBy(ICommandExecutor player, ICommandExecutor whisperer)
	{
		lastWhisperList.put(player.getName(), whisperer.getName());
	}

	private void setLastWhisperedByServer(ICommandExecutor player)
	{
		lastWhisperList.put(player.getName(), SERVER);
	}

	public boolean wasWhisperedByServer(ICommandExecutor player)
	{
		return lastWhisperList.containsKey(player.getName()) && lastWhisperList.get(player.getName()).equals(SERVER);
	}

	public void deleteLastWhisperedBy(ICommandExecutor player)
	{
		if (lastWhisperList.containsKey(player.getName()))
			lastWhisperList.remove(player.getName());
	}

	@Nullable
	public IPlayer getLastWhisperedBy(ICommandExecutor player)
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

	public static boolean blockWhisper(IPlayerVisibility player, IPlayer target)
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
	private final ChannelManager manager;
}
