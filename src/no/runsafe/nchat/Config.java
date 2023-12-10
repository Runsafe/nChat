package no.runsafe.nchat;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;

public class Config implements IConfigurationChanged
{
	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		// ChannelManager config
		channelTimeoutSeconds = config.getConfigValueAsInt("channel.timeoutSeconds");

		channelJoinMessage = config.getConfigValueAsString("channel.message.join");
		channelLeaveMessage = config.getConfigValueAsString("channel.message.leave");
		channelPrivateMessage = config.getConfigValueAsString("channel.message.private");
		channelSwitchMessage = config.getConfigValueAsString("channel.message.switch");
	}

	// ChannelManager config
	public static int channelTimeoutSeconds;
	public static String channelJoinMessage;
	public static String channelLeaveMessage;
	public static String channelPrivateMessage;
	public static String channelSwitchMessage;
}
