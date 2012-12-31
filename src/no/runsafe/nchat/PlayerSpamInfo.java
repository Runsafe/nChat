package no.runsafe.nchat;

import java.util.HashMap;

public class PlayerSpamInfo
{
	public PlayerSpamInfo()
	{
		this.messageHistory = new HashMap<Long, String>();
		this.hasFloodTimerRunning = false;
	}

	public int currentFloodCount()
	{
		return this.messageHistory.size();
	}

	public boolean isFloodTimerRunning()
	{
		return this.hasFloodTimerRunning;
	}

	public void addMessageToHistory(String message)
	{
		this.messageHistory.put(System.currentTimeMillis() / 1000L, message);
	}

	public void startFloodTimer()
	{
		this.hasFloodTimerRunning = true;
	}

	public void flushHistory()
	{
		this.messageHistory.clear();
		this.hasFloodTimerRunning = false;
	}

	private HashMap<Long, String> messageHistory;
	private boolean hasFloodTimerRunning;
}
