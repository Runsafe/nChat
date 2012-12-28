package no.runsafe.nchat;

import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import org.bukkit.configuration.ConfigurationSection;

public class ChatChannelHandler implements IConfigurationChanged
{
    public ChatChannelHandler(IConfiguration configuration, RunsafeServer server, ChatHandler chatHandler, IOutput output)
    {
        this.configuration = configuration;
        this.chatHandler = chatHandler;
        this.server = server;
        this.output = output;
        this.loadConfiguration();
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

    private void loadConfiguration()
    {
        this.channels = this.configuration.getSection("channels");
        this.channelTagFormat = this.configuration.getConfigValueAsString("nChat.channelTagFormat");
    }

    @Override
    public void OnConfigurationChanged()
    {
        this.loadConfiguration();
    }

    private String channelTagFormat;
    private IConfiguration configuration;
    private ConfigurationSection channels;
    private ChatHandler chatHandler;
    private RunsafeServer server;
    private IOutput output;
}
