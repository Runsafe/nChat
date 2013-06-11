package no.runsafe.nchat;

public class PlayerSpamInfo
{
	public PlayerSpamInfo()
	{
		this.hasFloodTimerRunning = false;
		this.hasRepeatTimerRunning = false;
		this.duplicateMessages = 0;
		this.floodingMessages = 0;
	}

	public void setLastMessage(String message)
	{
		if (message.equalsIgnoreCase(this.lastMessage))
			this.duplicateMessages = this.duplicateMessages + 1;
		else
			this.duplicateMessages = 0;

		this.floodingMessages = this.floodingMessages + 1;
		this.lastMessage = message;
	}

	public void resetFloodCount()
	{
		this.floodingMessages = 0;
		this.hasFloodTimerRunning = false;
	}

	public void resetPlayerRepeating()
	{
		this.duplicateMessages = 0;
		this.hasRepeatTimerRunning = false;
	}

	public int currentFloodCount()
	{
		return this.floodingMessages;
	}

	public int currentRepeatCount()
	{
		return this.duplicateMessages;
	}

	public boolean isFloodTimerRunning()
	{
		return this.hasFloodTimerRunning;
	}

	public boolean isRepeatTimerRunning()
	{
		return this.hasRepeatTimerRunning;
	}

	public void startFloodTimer()
	{
		this.hasFloodTimerRunning = true;
	}

	public void startRepeatTimer()
	{
		this.hasRepeatTimerRunning = true;
	}

	private String lastMessage;
	private int duplicateMessages;
	private int floodingMessages;

	private boolean hasFloodTimerRunning;
	private boolean hasRepeatTimerRunning;
}
