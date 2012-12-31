package no.runsafe.nchat;

import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.framework.timer.IScheduler;

import java.util.HashMap;

public class PlayerSpamInfo
{
	public PlayerSpamInfo()
	{
		this.messageHistory = new HashMap<Long, String>();
	}

	public int currentFloodCount()
	{
		return this.messageHistory.size();
	}

	public boolean hasFloodTimerRunning()
	{
		return this.hasFloodTimerRunning;
	}

	public void addMessageToHistory(String message)
	{
		this.messageHistory.put(System.currentTimeMillis() / 1000L, message);

		if (!this.hasFloodTimerRunning)
			this.hasFloodTimerRunning = true;
	}

	public void flushHistory()
	{
		this.messageHistory.clear();
		this.hasFloodTimerRunning = false;
	}

	private RunsafePlayer player;
	private HashMap<Long, String> messageHistory;
	private boolean hasFloodTimerRunning = false;
}
