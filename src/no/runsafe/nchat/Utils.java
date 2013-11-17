package no.runsafe.nchat;

import java.util.HashMap;
import java.util.Map;

public class Utils
{
	public static String mapReplace(String theString, HashMap<String, String> replaceMap)
	{
		for (Map.Entry<String, String> stringStringEntry : replaceMap.entrySet())
		{
			Map.Entry entry = (Map.Entry) stringStringEntry;
			theString = theString.replaceAll((String) entry.getKey(), (String) entry.getValue());
		}
		return theString;
	}
}
