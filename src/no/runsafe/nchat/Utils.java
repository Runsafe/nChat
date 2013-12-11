package no.runsafe.nchat;

import java.util.HashMap;
import java.util.Map;

public class Utils
{
	public static String mapReplace(String theString, HashMap<String, String> replaceMap)
	{
		if (theString == null)
			return null;
		for (Map.Entry<String, String> entry : replaceMap.entrySet())
			theString = theString.replaceAll(entry.getKey(), entry.getValue());
		return theString;
	}
}
