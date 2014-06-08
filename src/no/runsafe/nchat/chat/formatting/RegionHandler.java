package no.runsafe.nchat.chat.formatting;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class RegionHandler implements IConfigurationChanged
{
	public RegionHandler()//IRegionControl worldGuard)
	{
		//this.worldGuard = worldGuard;
	}

	@Nullable
	public String getRegionTag(IPlayer player)
	{
		String worldName = player.getWorldName();
		List<String> regions = null;//worldGuard.getRegionsAtLocation(player.getLocation());

		if (regions != null)
		{
			for (String region : regions)
			{
				String regionName = worldName + '-' + region;
				if (regionTags.containsKey(regionName))
					return regionTags.get(regionName);
			}
		}
		return null;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		regionTags = configuration.getConfigValuesAsMap("regionPrefixes");
	}

	private Map<String, String> regionTags;
//	private final IRegionControl worldGuard;
}
