package no.runsafe.nchat.connect;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.log.IConsole;
import no.runsafe.nchat.channel.ChannelManager;
import org.picocontainer.Startable;

public class PipeHandler implements Startable, IConfigurationChanged
{
	public PipeHandler(ChannelManager manager, IConsole console, IOutput output)
	{
		PipeHandler.manager = manager;
		PipeHandler.output = output;
		this.console = console;
	}

	@Override
	public void start()
	{
		if (!enabled) return;
		engine = new PipeEngine(console);
		thread = new Thread(engine);
		thread.start();
	}

	@Override
	public void stop()
	{
		try
		{
			engine.close();
			thread.interrupt();
			thread.join(2000);
		}
		catch (Exception e)
		{
			console.logException(e);
		}
	}

	public static void handleMessage(String message, String channel)
	{
		if (channel.equals("global") && engine != null)
			engine.sendMessage(message);
	}

	public static void takeMessage(String message)
	{
		output.broadcastColoured(message);
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		prefix = configuration.getConfigValueAsString("connect.prefix");
		enabled = configuration.getConfigValueAsBoolean("enableChatBridge");
	}

	public static String prefix;
	public static ChannelManager manager;
	private boolean enabled = false;
	private Thread thread;
	private final IConsole console;
	private static IOutput output;
	private static PipeEngine engine;
}
