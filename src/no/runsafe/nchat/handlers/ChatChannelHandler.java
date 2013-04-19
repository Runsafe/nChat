package no.runsafe.nchat.handlers;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.output.ChatColour;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;

import java.util.Map;

public class ChatChannelHandler implements IConfigurationChanged
{
	public ChatChannelHandler(RunsafeServer server, ChatHandler chatHandler)
	{
		this.chatHandler = chatHandler;
		this.server = server;
	}

	public boolean channelExists(String channelName)
	{
		return this.channels.containsKey(channelName);
	}

	public void broadcastMessage(String channelName, String message, RunsafePlayer player)
	{
		String permissionNode = String.format(Constants.CHAT_CHANNEL_NODE, channelName);
		String channelTag = String.format(this.channelTagFormat, this.channels.get(channelName));
		String formattedMessage = this.chatHandler.formatChatMessage(message.trim(), player).replaceAll(Constants.FORMAT_CHANNEL, ChatColour.ToMinecraft(channelTag));
		this.server.broadcastMessage(formattedMessage, permissionNode);
	}

	public boolean canTalkInChannel(String channelName, RunsafePlayer player)
	{
		String permissionNode = String.format(Constants.CHAT_CHANNEL_NODE, channelName);
		return player.hasPermission(permissionNode);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.channels = configuration.getConfigValuesAsMap("channels");
		this.channelTagFormat = configuration.getConfigValueAsString("nChat.channelTagFormat");
	}

	private String channelTagFormat;
	private Map<String, String> channels;
	private final ChatHandler chatHandler;
	private final RunsafeServer server;
}
