package no.runsafe.nchat.handlers;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import org.bukkit.configuration.ConfigurationSection;

public class ChatChannelHandler implements IConfigurationChanged
{
	public ChatChannelHandler(IConfiguration configuration, RunsafeServer server, ChatHandler chatHandler, IOutput output)
	{
		this.configuration = configuration;
		this.chatHandler = chatHandler;
		this.server = server;
	}

	public boolean channelExists(String channelName)
	{
		return this.channels.contains(channelName);
	}

	public void broadcastMessage(String channelName, String message, RunsafePlayer player)
	{
		String permissionNode = String.format(Constants.CHAT_CHANNEL_NODE, channelName);
		String channelTag = String.format(this.channelTagFormat, this.configuration.getConfigValueAsString("channels." + channelName + ".name"));
		String formattedMessage = this.chatHandler.formatChatMessage(message.trim(), player).replaceAll(Constants.FORMAT_CHANNEL, this.chatHandler.convertColors(channelTag));

		this.server.broadcastMessage(formattedMessage, permissionNode);
	}

	public boolean canTalkInChannel(String channelName, RunsafePlayer player)
	{
		String permissionNode = String.format(Constants.CHAT_CHANNEL_NODE, channelName);
		return player.hasPermission(permissionNode);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		// TODO: Check these don't need to be loaded on construct
		this.channels = iConfiguration.getSection("channels");
		this.channelTagFormat = iConfiguration.getConfigValueAsString("nChat.channelTagFormat");
	}

	private String channelTagFormat;
	private IConfiguration configuration;
	private ConfigurationSection channels;
	private ChatHandler chatHandler;
	private RunsafeServer server;
}
