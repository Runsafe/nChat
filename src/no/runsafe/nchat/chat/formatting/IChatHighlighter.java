package no.runsafe.nchat.chat.formatting;

import no.runsafe.framework.api.player.IPlayer;

public interface IChatHighlighter
{
	String highlight(IPlayer contextPlayer, String message);
}
