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

	public void close()
	{
		halt = true;
		try
		{
			server.close();
		}
		catch (IOException e)
		{
			// ignored
		}
	}

	@Override
	public void run()
	{
		if (server == null)
			return;

		while (!Thread.interrupted())
		{
			try
			{
				Socket socket = server.accept();
				if (halt)
				{
					try
					{
						socket.close();
					}
					catch (Exception e)
					{
						// ignored
					}
					break;
				}
				console.logInformation("Chat connection spawned for " + socket.getInetAddress().getHostName());
				Pipe pipe = new Pipe(socket);
				pipes.add(pipe);
				Thread thread = new Thread(pipe);
				thread.start();
				threads.add(thread);
			}
			catch (IOException e)
			{
				// Cancel.
			}
		}
		stop();
	}

	public void stop()
	{
		console.logInformation("Chat connection server shutting down");
		for (Pipe pipe : pipes)
		{
			pipe.close();
		}
		for (Thread thread : threads)
		{
			try
			{
				if (thread.isAlive())
				{
					thread.interrupt();
				}
			}
			catch (Exception e)
			{
				// ignored
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

	private final List<Pipe> pipes = new ArrayList<>(0);
	private final List<Thread> threads = new ArrayList<>(0);
	private ServerSocket server;
	private Boolean halt;
	private final IConsole console;
}
