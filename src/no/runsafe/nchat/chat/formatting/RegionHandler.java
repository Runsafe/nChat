package no.runsafe.nchat.chat.formatting;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.worldguardbridge.IRegionControl;

import java.util.List;
import java.util.Map;

public class RegionHandler implements IConfigurationChanged
{
	public RegionHandler(IRegionControl worldGuard)
	{
		this.worldGuard = worldGuard;
	}

	public String getRegionTag(IPlayer player)
	{
		String worldName = player.getWorldName();
		List<String> regions = this.worldGuard.getRegionsAtLocation(player.getLocation());

		if (regions != null)
		{
			for (String region : regions)
			{
				String regionName = worldName + "-" + region;
				if (this.regionTags.containsKey(regionName))
					return this.regionTags.get(regionName);
			}
		}
		return null;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration iConfiguration)
	{
		this.regionTags = iConfiguration.getConfigValuesAsMap("regionPrefixes");
	}

	private Map<String, String> regionTags;
	private final IRegionControl worldGuard;
}
