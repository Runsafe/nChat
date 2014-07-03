package no.runsafe.nchat.connect;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.nchat.channel.ChannelManager;
import org.picocontainer.Startable;

public class PipeHandler implements Startable, IConfigurationChanged
{
	public PipeHandler(ChannelManager manager, IConsole console)
	{
		PipeHandler.manager = manager;
		this.console = console;
	}

	@Override
	public void start()
	{
		engine = new PipeEngine(console);
		(new Thread(engine)).start();
	}

	@Override
	public void stop()
	{
		// Do nothing?
	}

	public static void handleMessage(String message, String channel)
	{
		if (channel.equals("global") && engine != null)
			engine.sendMessage(message);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		prefix = configuration.getConfigValueAsString("connect.prefix");
	}

	public static String prefix;
	public static ChannelManager manager;
	private IConsole console;
	private static PipeEngine engine;
}
