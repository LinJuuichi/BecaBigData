package com.everis.bbd.utilities.dictionary;

import java.util.logging.Logger;

/**
 * Removes all the text if has one of the words in the dictionary.
 */
public class BlackListDictionary extends Dictionary 
{

	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(BlackListDictionary.class.getName());
	
	/**
	 * Constructor.
	 */
	public BlackListDictionary() 
	{
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param name name of the dictionary.
	 */
	public BlackListDictionary(String name)
	{
		super(name);
	}

	@Override
	public String processText(String text) 
	{
		for (String word: _dictionary.keySet())
		{
			if (text.contains(word))
			{
				return "";
			}
		}
		return text;
	}

}
