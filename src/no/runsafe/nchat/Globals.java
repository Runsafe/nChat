package no.runsafe.nchat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Globals
{
    public String joinList(List<?> list)
    {
        StringBuilder returnString = new StringBuilder();

        for (Iterator<?> i = list.iterator(); i.hasNext();)
        {
            Object item = i.next();
            returnString.append(item);
        }
        return returnString.toString();
    }

    public String joinStringArray(String[] array)
    {
        StringBuilder returnString = new StringBuilder();

        for (int i = 0; i < array.length; i++)
            returnString.append(array[i]).append(" ");

        return returnString.toString();
    }

    public String mapReplace(String theString, HashMap<String, String> replaceMap)
    {
        for (Iterator<?> i = replaceMap.entrySet().iterator(); i.hasNext();)
        {
            Map.Entry entry = (Map.Entry) i.next();
            theString = theString.replaceAll((String) entry.getKey(), (String) entry.getValue());
        }
        return theString;
    }
}
