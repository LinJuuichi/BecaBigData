package com.everis.bbd.utilities.dictionary;

/**
 * Dictionary that removes all URL like words.
 *
 */
public class NoURLDictionary extends Dictionary 
{
	
	/**
	 * Constructor.
	 * 
	 * @param name name of the dictionary.
	 */
	public NoURLDictionary(String name)
	{
		super(name);
	}

	@Override
	public String processText(String text) 
	{
		String textResult = "";
		String[] splitText = text.split(" ");
		for (int i = 0; i < splitText.length; ++i)
		{
			boolean put = true;
			for (String word: _dictionary.keySet())
			{
				if (splitText[i].contains(word))
				{
					put = false;
					break;
				}
			}
			if (put)
			{
				textResult += splitText[i]+" ";
			}
		}
		return textResult;
	}
}
