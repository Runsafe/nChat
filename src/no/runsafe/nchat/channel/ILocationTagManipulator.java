package no.runsafe.nchat.channel;

import no.runsafe.framework.api.command.ICommandExecutor;

import javax.annotation.Nonnull;

public interface ILocationTagManipulator
{
	@Nonnull
	String getLocationTag(ICommandExecutor player, @Nonnull String tag);
}
