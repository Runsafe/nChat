package no.runsafe.nchat.command;

import no.runsafe.framework.command.RunsafeCommand;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.output.IOutput;
import no.runsafe.framework.server.RunsafeServer;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.nchat.Constants;
import no.runsafe.nchat.handlers.MuteHandler;
import org.bukkit.ChatColor;

public class UnMuteCommand extends RunsafeCommand
{
	public UnMuteCommand(IOutput console, MuteHandler muteHandler)
	{
		super("unmute", "player");
		this.console = console;
		this.muteHandler = muteHandler;
	}

	@Override
	public String requiredPermission()
	{
		return "runsafe.nchat.mute";
	}

	@Override
	public String OnExecute(RunsafePlayer executor, String[] args)
	{
		String unMutePlayerName = args[0].trim();

		if (unMutePlayerName.equalsIgnoreCase("server"))
		{
			if (executor.hasPermission("nChat.commands.muteServer"))
			{
				this.muteHandler.unMuteServer();
				executor.sendMessage(Constants.DEFAULT_MESSAGE_COLOR + Constants.COMMAND_CHAT_UNMUTED);
				console.write(String.format("%s un-muted server chat.", executor.getName()));
			}
			else
			{
				executor.sendMessage(Constants.COMMAND_NO_PERMISSION);
			}
		}
		else
		{
			if (executor.hasPermission("nChat.commands.mutePlayer"))
			{
				RunsafePlayer unMutePlayer = RunsafeServer.Instance.getPlayer(unMutePlayerName);

				if (unMutePlayer != null)
				{
					if (!unMutePlayer.hasPermission("nChat.muteExempt"))
					{
						console.write(String.format(
								"%s un-muted %s",
								executor.getName(),
								unMutePlayer.getName()
						));
						this.muteHandler.unMutePlayer(unMutePlayer);
					}
					else
					{
						executor.sendMessage(Constants.COMMAND_TARGET_EXEMPT);
					}
				}
				else
				{
					executor.sendMessage(Constants.COMMAND_TARGET_NO_EXISTS);
				}
			}
			else
			{
				executor.sendMessage(Constants.COMMAND_NO_PERMISSION);
			}
		}

		return null;
	}

	@Override
	public boolean Execute(RunsafePlayer player, String[] args)
	{
		if (args.length == 1)
		{
			String unMutePlayerName = args[0].trim();

			if (!unMutePlayerName.isEmpty())
			{
				if (unMutePlayerName.equalsIgnoreCase("server"))
				{
					if (player.hasPermission("nChat.commands.muteServer"))
					{
						this.muteHandler.unMuteServer();
						player.sendMessage(Constants.DEFAULT_MESSAGE_COLOR + Constants.COMMAND_CHAT_UNMUTED);
						console.write(String.format("%s un-muted server chat.", player.getName()));
					}
					else
					{
						player.sendMessage(Constants.COMMAND_NO_PERMISSION);
					}
				}
				else
				{
					if (player.hasPermission("nChat.commands.mutePlayer"))
					{
						RunsafePlayer unMutePlayer = RunsafeServer.Instance.getPlayer(unMutePlayerName);

						if (unMutePlayer != null)
						{
							if (!unMutePlayer.hasPermission("nChat.muteExempt"))
							{
								console.write(String.format(
										"%s un-muted %s",
										player.getName(),
										unMutePlayer.getName()
								));
								this.muteHandler.unMutePlayer(unMutePlayer);
							}
							else
							{
								player.sendMessage(Constants.COMMAND_TARGET_EXEMPT);
							}
						}
						else
						{
							player.sendMessage(Constants.COMMAND_TARGET_NO_EXISTS);
						}
					}
					else
					{
						player.sendMessage(Constants.COMMAND_NO_PERMISSION);
					}
				}
			}
			else
			{
				player.sendMessage(Constants.COMMAND_ENTER_PLAYER);
			}

			return true;
		}
		return false;
	}

	private IOutput console;
	private MuteHandler muteHandler;
}
