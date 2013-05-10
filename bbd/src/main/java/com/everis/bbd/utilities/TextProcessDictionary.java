package com.everis.bbd.utilities;

/**
 * Wrapper for dictionary that process text lines.
 */
public class TextProcessDictionary extends Dictionary 
{
	/**
	 * Process the line.
	 * Removes the characters from char dictionary.
	 * Removes the words from word dictionary.
	 * Substitutes the words in list dictionary.
	 * 
	 * @param line to process.
	 * @return processed text.
	 */
	public String preProcess(String line)
	{
		line = line.toLowerCase();
		for (Object o: _charDictionary.toArray())
		{
			String s = (String) o;
			line = line.replaceAll("\\"+s, " ");
		}
		
		for (Object o: _wordDictionary.toArray())
		{
			String s = (String) o;
			line = line.replaceAll(" "+s+" ", " ");
		}
		
		for (String s: line.split(" "))
		{
			String replaceWord = replaceWord(s);
			if (replaceWord != null)
			{
				line = line.replace(" "+s, " "+replaceWord);
			}
		}
		
		return line;
	}
}
