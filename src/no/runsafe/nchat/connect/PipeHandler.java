package no.runsafe.nchat.connect;

import no.runsafe.framework.api.log.IConsole;
import no.runsafe.nchat.channel.ChannelManager;
import org.picocontainer.Startable;

public class PipeHandler implements Startable
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

	public static ChannelManager manager;
	private IConsole console;
	private static PipeEngine engine;
}
