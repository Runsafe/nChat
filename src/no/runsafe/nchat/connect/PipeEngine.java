package no.runsafe.nchat.connect;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.log.IConsole;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PipeEngine implements Runnable, IConfigurationChanged
{
	public PipeEngine(IConsole console)
	{
		this.console = console;
	}

	@Override
	public void run()
	{
		listenToPort();
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

	@Override
	public void OnConfigurationChanged(IConfiguration config)
	{
		port = config.getConfigValueAsInt("chatBridgePort");
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

	private void listenToPort()
	{
		if (port == 0) return;
		try
		{
			server = new ServerSocket(port);
			console.logInformation("nChat Connect listening on port "+ port);
		}
		catch (IOException e)
		{
			console.logException(e);
		}
	}

	private static int port = 0;
	private final List<Pipe> pipes = new ArrayList<Pipe>(0);
	private ServerSocket server;
	private final IConsole console;
}
