package no.runsafe.nchat.connect;

import no.runsafe.framework.text.ChatColour;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Pipe implements Runnable
{
	public Pipe(Socket client)
	{
		this.client = client;
	}

	@Override
	public void run()
	{
		try
		{
			new Thread(new PipeIn(client)).start();
			while (true)
			{
				if (stream == null)
					stream = new PrintWriter(client.getOutputStream(), true);

				String message = ChatColour.ToMinecraft(PipeHandler.prefix + ' ' + chatTunnel.take()); // Grab message.
				stream.println(message);
			}
		}
		catch (Exception e)
		{
			// Terminate thread.
		}
	}

	public final LinkedBlockingQueue<String> chatTunnel = new LinkedBlockingQueue<>();
	private final Socket client;
	private PrintWriter stream;
}
