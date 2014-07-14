package no.runsafe.nchat.connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PipeIn implements Runnable
{
	public PipeIn(Socket client)
	{
		this.client = client;
	}

	@Override
	public void run()
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String output;

			while ((output = reader.readLine()) != null)
				PipeHandler.takeMessage(output);
		}
		catch (IOException e)
		{
			// Poop!
		}
	}

	private final Socket client;
}
