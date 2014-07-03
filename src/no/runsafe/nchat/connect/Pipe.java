package no.runsafe.nchat.connect;

import java.io.DataOutputStream;
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
			while (true)
			{
				if (stream == null)
					stream = new DataOutputStream(client.getOutputStream());

				String message = chatTunnel.take(); // Grab message.
				stream.writeUTF(message);
			}
		}
		catch (Exception e)
		{
			// Terminate thread.
		}
	}

	public final LinkedBlockingQueue<String> chatTunnel = new LinkedBlockingQueue<String>(0);
	private final Socket client;
	private DataOutputStream stream;
}
