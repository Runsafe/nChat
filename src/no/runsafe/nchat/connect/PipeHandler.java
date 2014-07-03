package no.runsafe.nchat.connect;

import no.runsafe.framework.api.log.IConsole;
import no.runsafe.nchat.channel.ChannelManager;

public class PipeHandler
{
	public PipeHandler(ChannelManager manager, IConsole console)
	{
		PipeHandler.manager = manager;
		engine = new PipeEngine(console);

		(new Thread(engine)).start();
	}

	public static void handleMessage(String message, String channel)
	{
		if (channel.equals("global") && engine != null)
			engine.sendMessage(message);
	}

	public static ChannelManager manager;
	private static PipeEngine engine;
}
