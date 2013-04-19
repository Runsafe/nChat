package no.runsafe.nchat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Globals
{
	public String joinList(List<?> list)
	{
		StringBuilder returnString = new StringBuilder();

		for (Object item : list)
		{
			returnString.append(item);
		}
		return returnString.toString();
	}

	public String joinStringArray(String[] array)
	{
		StringBuilder returnString = new StringBuilder();

		for (String anArray : array)
			returnString.append(anArray).append(" ");

		return returnString.toString();
	}

	public String mapReplace(String theString, HashMap<String, String> replaceMap)
	{
		for (Map.Entry<String, String> stringStringEntry : replaceMap.entrySet())
		{
			Map.Entry entry = (Map.Entry) stringStringEntry;
			theString = theString.replaceAll((String) entry.getKey(), (String) entry.getValue());
		}
		return theString;
	}
}
