package no.runsafe.nchat.connect;

import no.runsafe.framework.api.log.IConsole;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PipeEngine implements Runnable
{
	public PipeEngine(IConsole console)
	{
		this.console = console;
		try
		{
			server = new ServerSocket(19242);
			console.logInformation("nChat Connect listening on port 19242");
		}
		catch (IOException e)
		{
			console.logException(e);
		}
	}

	@Override
	public void run()
	{
		if (server == null)
			return;

		while (true)
		{
			try
			{
				Socket socket = server.accept();
				console.logInformation("Chat connection spawned for " + socket.getInetAddress().getHostName());
				Pipe pipe = new Pipe(socket);
				pipes.add(pipe);
				new Thread(pipe).start();
			}
			catch (IOException e)
			{
				// Cancel.
			}
		}
	}

	public void sendMessage(String message)
	{
		for (Pipe pipe : pipes)
		{
			try
			{
				pipe.chatTunnel.put(message);
			}
			catch (InterruptedException e)
			{
				pipes.remove(pipe);
			}
		}
	}

	private final List<Pipe> pipes = new ArrayList<Pipe>(0);
	private ServerSocket server;
	private final IConsole console;
}
