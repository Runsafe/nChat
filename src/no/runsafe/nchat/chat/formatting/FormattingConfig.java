package no.runsafe.nchat.chat.formatting;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;

import java.util.Map;

public class FormattingConfig implements IConfigurationChanged
{
	public String getOpTagFormat()
	{
		return opTagFormat;
	}

	public String getBanTagFormat()
	{
		return banTagFormat;
	}

	public String getPlayerChatFormat()
	{
		return playerChatMessage;
	}

	public String getPlayerNameFormat()
	{
		return playerNameFormat;
	}

	public Map<String, String> getChatGroupPrefixes()
	{
		return chatGroupPrefixes;
	}

	public Map<String, String> getWorldPrefixes()
	{
		return worldPrefixes;
	}

	public boolean regionPrefixesEnabled()
	{
		return enableRegionPrefixes;
	}

	public boolean colorCodesEnabled()
	{
		return enableColorCodes;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		chatGroupPrefixes = configuration.getConfigValuesAsMap("chatGroupPrefixes");
		worldPrefixes = configuration.getConfigValuesAsMap("worldPrefixes");
		opTagFormat = configuration.getConfigValueAsString("chatFormatting.opTagFormat");
		banTagFormat = configuration.getConfigValueAsString("chatFormatting.banTagFormat");
		playerNameFormat = configuration.getConfigValueAsString("chatFormatting.playerName");
		playerChatMessage = configuration.getConfigValueAsString("chatFormatting.playerChatMessage");
		enableRegionPrefixes = configuration.getConfigValueAsBoolean("nChat.enableRegionPrefixes");
		enableColorCodes = configuration.getConfigValueAsBoolean("nChat.enableColorCodes");
	}

	private Map<String, String> chatGroupPrefixes;
	private Map<String, String> worldPrefixes;
	private String opTagFormat;
	private String banTagFormat;
	private String playerChatMessage;
	private String playerNameFormat;
	private boolean enableRegionPrefixes;
	private boolean enableColorCodes;
}
