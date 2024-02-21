package no.runsafe.nchat.connect;

import no.runsafe.framework.text.ChatColour;

import java.io.IOException;
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
			Thread thread = new Thread(new PipeIn(client));
			thread.start();
			while (!Thread.interrupted())
			{
				if (stream == null)
					stream = new PrintWriter(client.getOutputStream(), true);

				String message = ChatColour.ToMinecraft(PipeHandler.prefix + ' ' + chatTunnel.take()); // Grab message.
				stream.println(message);
			}
			thread.interrupt();
			thread.join(200);
		}
		catch (Exception e)
		{
			// Terminate thread.
		}
	}

	public final LinkedBlockingQueue<String> chatTunnel = new LinkedBlockingQueue<>();
	private final Socket client;
	private PrintWriter stream;

	public void close()
	{
		try
		{
			client.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
