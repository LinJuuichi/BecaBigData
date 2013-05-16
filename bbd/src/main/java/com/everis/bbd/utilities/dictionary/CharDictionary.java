package com.everis.bbd.utilities.dictionary;

import java.util.logging.Logger;

/**
 * Char dictionary.
 */
public class CharDictionary extends Dictionary 
{
	/**
	 * Logger.
	 */
	protected static Logger log = Logger.getLogger(CharDictionary.class.getName());
	
	/**
	 * Constructor.
	 */
	public CharDictionary() 
	{
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param name name of the dictionary.
	 */
	public CharDictionary(String name)
	{
		super(name);
	}

	@Override
	public String processText(String text) 
	{
		text = text.toLowerCase();
		for (String character: _dictionary.keySet())
		{
			text = text.replaceAll("\\"+character, " ");
		}
		return text;
	}
}
