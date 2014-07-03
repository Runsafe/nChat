package no.runsafe.nchat.connect;

import no.runsafe.framework.api.log.IConsole;

import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Pipe implements Runnable
{
	public Pipe(Socket client, IConsole console)
	{
		this.client = client;
		this.console = console;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				if (stream == null)
					stream = new PrintWriter(client.getOutputStream(), true);

				String message = chatTunnel.take(); // Grab message.
				stream.println(message);
			}
		}
		catch (Exception e)
		{
			// Terminate thread.
		}
	}

	public final LinkedBlockingQueue<String> chatTunnel = new LinkedBlockingQueue<String>();
	private final Socket client;
	private final IConsole console;
	private PrintWriter stream;
}
